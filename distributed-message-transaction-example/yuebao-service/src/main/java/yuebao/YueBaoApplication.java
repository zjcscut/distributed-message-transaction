package yuebao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.throwable.client.common.LocalTransactionStatusEnum;
import org.throwable.client.model.DestinationMessage;
import org.throwable.client.model.MessageTransaction;
import org.throwable.client.support.LocalTransactionExecutor;
import org.throwable.client.support.MessageTransactionTemplate;
import org.throwable.utils.JacksonUtils;
import yuebao.dto.AccountDTO;
import yuebao.entity.Account;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/12 0:25
 */
@SpringBootApplication
public class YueBaoApplication implements CommandLineRunner {

	private static final RowMapper<Account> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Account.class);

	private static final Random RANDOM = new Random();

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	MessageTransactionTemplate messageTransactionTemplate;

	public static void main(String[] args) {
		SpringApplication.run(YueBaoApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		List<Account> accounts = jdbcTemplate.query("SELECT * FROM t_account", ROW_MAPPER);
		List<AccountDTO> dtos = new ArrayList<>(accounts.size());
		for (Account account : accounts) {
			AccountDTO dto = new AccountDTO();
			dto.setUserId(account.getUserId());
			dto.setAmount(RANDOM.nextInt(1000));
			account.setAmount(account.getAmount() - dto.getAmount());
			dtos.add(dto);
		}
		for (AccountDTO dto : dtos) {
			MessageTransaction transaction = new MessageTransaction();
			transaction.setBusinessSign(dto.getUserId());
			List<DestinationMessage> destinationMessages = new ArrayList<>(1);
			DestinationMessage destinationMessage = new DestinationMessage();
			destinationMessage.setDestination("account.yuebao->zhifubao");
			destinationMessage.setContent(JacksonUtils.INSTANCE.toJson(dto));
			destinationMessages.add(destinationMessage);
			transaction.setDestinationMessages(destinationMessages);
			LocalTransactionExecutor executor = () -> {
				Account account = getAccount(dto.getUserId(), accounts);
				jdbcTemplate.update("UPDATE t_account SET amount = ? WHERE id = ?", new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, account.getAmount());
						ps.setInt(2, account.getId());
					}
				});
				return LocalTransactionStatusEnum.SUCCESS;
			};
			transaction.setLocalTransactionExecutor(executor);
			messageTransactionTemplate.processMessageTransaction(transaction);
		}
	}

	private Account getAccount(String userId, List<Account> accounts) {
		for (Account account : accounts) {
			if (account.getUserId().equals(userId)) {
				return account;
			}
		}
		throw new IllegalArgumentException("Not reach");
	}
}

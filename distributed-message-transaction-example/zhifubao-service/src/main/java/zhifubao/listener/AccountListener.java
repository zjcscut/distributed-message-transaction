package zhifubao.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.throwable.utils.JacksonUtils;
import zhifubao.dto.AccountDTO;
import zhifubao.entity.Account;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/12 13:40
 */
@Slf4j
@Component
public class AccountListener implements InitializingBean {

	private static final RowMapper<Account> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Account.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	TransactionTemplate transactionTemplate;

	@Autowired
	AmqpAdmin amqpAdmin;

	@Override
	public void afterPropertiesSet() throws Exception {
		Queue queue = new Queue("account.yuebao->zhifubao");
		DirectExchange exchange = new DirectExchange("account.yuebao->zhifubao");
		Binding with = BindingBuilder.bind(queue).to(exchange).with("account.yuebao->zhifubao");
		amqpAdmin.declareQueue(queue);
		amqpAdmin.declareExchange(exchange);
		amqpAdmin.declareBinding(with);
	}

	@RabbitListener(queues = "account.yuebao->zhifubao")
	public void onMessage(Message message) {
		try {
			String body = new String(message.getBody(), "UTF-8");
			AccountDTO accountDTO = JacksonUtils.INSTANCE.parse(body, AccountDTO.class);
			transactionTemplate.execute((TransactionCallback<Void>) transactionStatus -> {
				Account account = jdbcTemplate.queryForObject("SELECT * FROM t_account WHERE user_id = ?", ROW_MAPPER, accountDTO.getUserId());
				jdbcTemplate.update("UPDATE t_account SET amount = ? WHERE id = ?", new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, account.getAmount() + accountDTO.getAmount());
						ps.setInt(2, account.getId());
					}
				});
				return null;
			});

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}

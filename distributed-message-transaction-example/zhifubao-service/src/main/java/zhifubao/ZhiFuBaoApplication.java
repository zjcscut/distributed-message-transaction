package zhifubao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/12 0:28
 */
@SpringBootApplication
public class ZhiFuBaoApplication implements CommandLineRunner{

	@Autowired
	JdbcTemplate jdbcTemplate;

	public static void main(String[] args){
		SpringApplication.run(ZhiFuBaoApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {

	}
}

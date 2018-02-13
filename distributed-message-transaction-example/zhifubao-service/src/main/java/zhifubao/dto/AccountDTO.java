package zhifubao.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/12 0:35
 */
@ToString
@Data
public class AccountDTO {

	private String userId;
	private Integer amount;
}

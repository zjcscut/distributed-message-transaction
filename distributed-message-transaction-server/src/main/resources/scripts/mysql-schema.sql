CREATE TABLE `t_transaction_message_log`(
  id BIGINT UNSIGNED NOT NULL PRIMARY KEY,
  transaction_id VARCHAR(50) NOT NULL COMMENT '事务标识',
  business_sign VARCHAR(50) NOT NULL COMMENT '业务标记',
  application_name VARCHAR(50) NOT NULL COMMENT 'spring.application.name',
  instance_sign VARCHAR(50) NOT NULL COMMENT '实例标识',
  transaction_status TINYINT NOT NULL COMMENT '事务状态'
) COMMENT '事务消息日志表';

CREATE TABLE `t_transaction_message_content`(
  id BIGINT UNSIGNED NOT NULL PRIMARY KEY,
  message_log_id BIGINT UNSIGNED NOT NULL COMMENT '消息日志表主键',
  content VARCHAR(5000) COMMENT '消息内容',
  UNIQUE unique_message_log_id(message_log_id)
)COMMENT '事务消息内容表';
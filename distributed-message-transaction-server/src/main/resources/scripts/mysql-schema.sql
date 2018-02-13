
CREATE DATABASE IF NOT EXISTS `dmt_server` CHARSET utf8mb4 COLLATE utf8mb4_unicode_520_ci;

USE `dmt_server`;

CREATE TABLE `t_transaction_message_log`(
  id BIGINT UNSIGNED NOT NULL PRIMARY KEY,
  transaction_id VARCHAR(50) NOT NULL COMMENT '事务标识',
  business_sign VARCHAR(50) NOT NULL COMMENT '业务标记',
  application_name VARCHAR(50) NOT NULL COMMENT 'spring.application.name',
  instance_sign VARCHAR(50) NOT NULL COMMENT '实例标识',
  transaction_status TINYINT NOT NULL COMMENT '事务状态',
  global_status TINYINT NOT NULL COMMENT '全局状态',
  trigger_queue VARCHAR(255) NOT NULL DEFAULT 'NULL' COMMENT '事务触发队列名称',
  check_queue VARCHAR(255) NOT NULL DEFAULT 'NULL' COMMENT '事务查询队列名称',
  checker_class_name VARCHAR(255) NOT NULL DEFAULT 'NULL' COMMENT '事务查询类名称',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  trigger_time DATETIME NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '触发本地事务时间',
  confirm_time DATETIME NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '本地事务确认时间',
  push_time DATETIME NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '推送远程消息时间',
  UNIQUE unique_transaction_id(transaction_id),
  INDEX index_business_sign(business_sign),
  INDEX index_create_time(create_time),
  INDEX index_transaction_status(transaction_status),
  INDEX index_global_status(global_status)
) COMMENT '事务消息日志表';

CREATE TABLE `t_transaction_message_content`(
  id BIGINT UNSIGNED NOT NULL PRIMARY KEY,
  message_log_id BIGINT UNSIGNED NOT NULL COMMENT '消息日志表主键',
  content VARCHAR(5000) COMMENT '消息内容',
  UNIQUE unique_message_log_id(message_log_id)
)COMMENT '事务消息内容表';
package org.throwable.server.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.throwable.server.model.entity.MessageLog;

import java.sql.Timestamp;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/2 15:31
 */
@Repository
public class MysqlMessageLogRepository implements MessageLogRepository {

    private static final RowMapper<MessageLog> ROW_MAPPER = BeanPropertyRowMapper.newInstance(MessageLog.class);

    private final JdbcTemplate jdbcTemplate;

    public MysqlMessageLogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MessageLog findById(Long id) {
        String sql = "SELECT * FROM t_transaction_message_log WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
    }

    @Override
    public MessageLog findByTransactionId(String transactionId) {
        String sql = "SELECT * FROM t_transaction_message_log WHERE transaction_id = ?";
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, transactionId);
    }

    @Override
    public int save(final MessageLog messageLog) {
        String sql = "INSERT INTO t_transaction_message_log(id,transaction_id,business_sign,application_name,instance_sign" +
                ",transaction_status,global_status,trigger_queue,check_queue,checker_class_name) VALUES (?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setLong(1, messageLog.getId());
            preparedStatement.setString(2, messageLog.getTransactionId());
            preparedStatement.setString(3, messageLog.getBusinessSign());
            preparedStatement.setString(4, messageLog.getApplicationName());
            preparedStatement.setString(5, messageLog.getInstanceSign());
            preparedStatement.setInt(6, messageLog.getTransactionStatus());
            preparedStatement.setInt(7, messageLog.getGlobalStatus());
            preparedStatement.setString(8, messageLog.getTriggerQueue());
            preparedStatement.setString(9, messageLog.getCheckQueue());
            preparedStatement.setString(10, messageLog.getCheckerClassName());
        });
    }

    @Override
    public int updateForRegisterConfirm(final MessageLog messageLog) {
        String sql = "UPDATE t_transaction_message_log SET global_status = ?,trigger_time = ?,update_time = ? WHERE id = ?";
        return jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, messageLog.getGlobalStatus());
            preparedStatement.setTimestamp(2, new Timestamp(messageLog.getTriggerTime().getTime()));
            preparedStatement.setTimestamp(3, new Timestamp(messageLog.getUpdateTime().getTime()));
            preparedStatement.setLong(4, messageLog.getId());
        });
    }

    @Override
    public int updateForTransactionConfirm(MessageLog messageLog) {
        String sql = "UPDATE t_transaction_message_log SET transaction_status = ?,confirm_time = ?,update_time = ? WHERE id = ?";
        return jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, messageLog.getTransactionStatus());
            preparedStatement.setTimestamp(2, new Timestamp(messageLog.getConfirmTime().getTime()));
            preparedStatement.setTimestamp(3, new Timestamp(messageLog.getUpdateTime().getTime()));
            preparedStatement.setLong(4, messageLog.getId());
        });
    }

    @Override
    public int updateForPush(MessageLog messageLog) {
        String sql = "UPDATE t_transaction_message_log SET global_status = ?,push_time = ?,update_time = ? WHERE id = ?";
        return jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, messageLog.getGlobalStatus());
            preparedStatement.setTimestamp(2, new Timestamp(messageLog.getPushTime().getTime()));
            preparedStatement.setTimestamp(3, new Timestamp(messageLog.getUpdateTime().getTime()));
            preparedStatement.setLong(4, messageLog.getId());
        });
    }

    @Override
    public int updateForDelayConfirm(MessageLog messageLog) {
        String sql = "UPDATE t_transaction_message_log SET global_status = ?,update_time = ? WHERE id = ?";
        return jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, messageLog.getGlobalStatus());
            preparedStatement.setTimestamp(2, new Timestamp(messageLog.getUpdateTime().getTime()));
            preparedStatement.setLong(3, messageLog.getId());
        });
    }
}

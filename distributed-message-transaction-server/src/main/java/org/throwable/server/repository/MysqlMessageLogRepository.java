package org.throwable.server.repository;

import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.throwable.server.model.entity.MessageLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    public MessageLog findByTransactionId(String transactionId) {
        String sql = "SELECT * FROM t_transaction_message_log WHERE transaction_id = ?";
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, transactionId);
    }

    @Override
    public int save(final MessageLog messageLog) {
        String sql = "INSERT INTO t_transaction_message_log() VALUES ()";
        return jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setLong(1, messageLog.getId());
            preparedStatement.setLong(1, messageLog.getId());
            preparedStatement.setLong(1, messageLog.getId());
            preparedStatement.setLong(1, messageLog.getId());
            preparedStatement.setLong(1, messageLog.getId());
            preparedStatement.setLong(1, messageLog.getId());
        });
    }
}

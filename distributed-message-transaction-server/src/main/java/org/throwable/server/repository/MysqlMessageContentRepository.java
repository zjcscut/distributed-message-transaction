package org.throwable.server.repository;

import lombok.NonNull;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.throwable.server.model.entity.MessageContent;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/2 15:30
 */
@Repository
public class MysqlMessageContentRepository implements MessageContentRepository {

    private static final RowMapper<MessageContent> ROW_MAPPER = BeanPropertyRowMapper.newInstance(MessageContent.class);

    private final JdbcTemplate jdbcTemplate;

    public MysqlMessageContentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MessageContent findByMessageLogId(@NonNull Long id) {
        String sql = "SELECT * FROM t_transaction_message_content WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
    }
}

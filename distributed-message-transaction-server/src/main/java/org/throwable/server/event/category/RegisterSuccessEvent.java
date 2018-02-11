package org.throwable.server.event.category;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;
import org.throwable.server.model.entity.MessageLog;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/5 17:10
 */
@ToString
@Data
public class RegisterSuccessEvent extends ApplicationEvent {

    private MessageLog messageLog;

    public RegisterSuccessEvent(Object source) {
        super(source);
    }
}

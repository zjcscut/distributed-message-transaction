package org.throwable.server.event.category;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/6 12:31
 */
@ToString
@Data
public class TransactionDelayConfirmEvent extends ApplicationEvent {

    private Long id;

    public TransactionDelayConfirmEvent(Object source) {
        super(source);
    }
}

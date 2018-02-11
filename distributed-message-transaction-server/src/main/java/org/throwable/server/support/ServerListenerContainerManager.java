package org.throwable.server.support;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.InitializingBean;
import org.throwable.server.support.listener.MessageTransactionConfirmListener;
import org.throwable.server.support.listener.MessageTransactionDelayConfirmTaskListener;
import org.throwable.server.support.listener.MessageTransactionRegisterListener;
import org.throwable.support.ListenerContainerFactory;
import org.throwable.support.ListenerContainerRegistrar;

import java.util.HashSet;
import java.util.Set;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 19:14
 */
public class ServerListenerContainerManager implements InitializingBean {

    private static final Set<String> INTERNAL_LISTENERS = new HashSet<>();
    private final ListenerContainerRegistrar registrar;
    private final ListenerContainerFactory factory;

    static {
        INTERNAL_LISTENERS.add(MessageTransactionRegisterListener.class.getName());
        INTERNAL_LISTENERS.add(MessageTransactionConfirmListener.class.getName());
        INTERNAL_LISTENERS.add(MessageTransactionDelayConfirmTaskListener.class.getName());
    }

    public ServerListenerContainerManager(ListenerContainerRegistrar registrar, ListenerContainerFactory factory) {
        this.registrar = registrar;
        this.factory = factory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        registerInternalListeners();
    }

    public void register(String listenerClassName) throws Exception {
        SimpleMessageListenerContainer container = factory.createListenerContainer(listenerClassName);
        registrar.registerListenerContainer(container);
    }

    public void register(Class<?> listenerClass) throws Exception {
        SimpleMessageListenerContainer container = factory.createListenerContainer(listenerClass);
        registrar.registerListenerContainer(container);
    }

    private void registerInternalListeners() throws Exception {
        if (!INTERNAL_LISTENERS.isEmpty()) {
            for (String listenerClassName : INTERNAL_LISTENERS) {
                register(listenerClassName);
            }
        }
    }
}

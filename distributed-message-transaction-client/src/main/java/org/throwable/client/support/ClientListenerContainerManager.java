package org.throwable.client.support;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.InitializingBean;
import org.throwable.support.ListenerContainerFactory;
import org.throwable.support.ListenerContainerRegistrar;

import java.util.HashSet;
import java.util.Set;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 17:48
 */
public class ClientListenerContainerManager implements InitializingBean {

    private static final Set<String> INTERNAL_LISTENERS = new HashSet<>();
    private final ListenerContainerRegistrar registrar;
    private final ListenerContainerFactory factory;

    static {
        INTERNAL_LISTENERS.add(LocalTransactionCheckListener.class.getName());
        INTERNAL_LISTENERS.add(LocalTransactionTriggerListener.class.getName());
    }

    public ClientListenerContainerManager(ListenerContainerRegistrar registrar,
                                          ListenerContainerFactory factory) {
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

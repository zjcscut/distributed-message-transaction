package org.throwable.support;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 16:46
 */
public class NamedThreadFactory implements ThreadFactory {

    private final AtomicInteger counter = new AtomicInteger();
    private final String prefix;
    private final boolean daemon;
    private final int priority;
    private final ThreadGroup group;

    public NamedThreadFactory(String prefix) {
        this(prefix, false, Thread.NORM_PRIORITY);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        this(prefix, daemon, Thread.NORM_PRIORITY);
    }

    public NamedThreadFactory(String prefix, boolean daemon, int priority) {
        this.prefix = prefix;
        this.daemon = daemon;
        this.priority = priority;
        SecurityManager securityManager = System.getSecurityManager();
        this.group = (null == securityManager) ? Thread.currentThread().getThreadGroup() : securityManager.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String threadName = prefix + "-thread-" + counter.getAndIncrement();
        Thread thread = new Thread(group, runnable, threadName);
        thread.setDaemon(daemon);
        thread.setPriority(priority);
        return thread;
    }
}

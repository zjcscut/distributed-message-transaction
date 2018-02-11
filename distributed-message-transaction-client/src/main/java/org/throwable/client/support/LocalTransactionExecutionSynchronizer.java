package org.throwable.client.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author throwable
 * @version v1.0
 * @description Local transaction execution synchronizer
 * @since 2018/2/11 15:20
 */
abstract class LocalTransactionExecutionSynchronizer {

    private static final Map<String, BlockingLocalTransactionExecutorConsumer> EXECUTOR_CONSUMERS = new ConcurrentHashMap<>(256);
    private static final Map<String, LocalTransactionExecutorAdapter> LOCAL_TRANSACTION_EXECUTORS = new ConcurrentHashMap<>(256);

    public static void addTransactionExecutor(String transactionId, LocalTransactionExecutorAdapter transactionExecutor) {
        LOCAL_TRANSACTION_EXECUTORS.putIfAbsent(transactionId, transactionExecutor);
    }

    public static LocalTransactionExecutorAdapter getTransactionExecutor(String transactionId) {
        return LOCAL_TRANSACTION_EXECUTORS.get(transactionId);
    }

    public static boolean existTransactionExecutor(String transactionId) {
        return LOCAL_TRANSACTION_EXECUTORS.containsKey(transactionId);
    }

    public static void removeTransactionExecutor(String transactionId) {
        LOCAL_TRANSACTION_EXECUTORS.remove(transactionId);
    }


    public static void addTransactionConsumer(String transactionId, BlockingLocalTransactionExecutorConsumer consumer) {
        EXECUTOR_CONSUMERS.putIfAbsent(transactionId, consumer);
    }

    public static BlockingLocalTransactionExecutorConsumer getTransactionConsumer(String transactionId) {
        return EXECUTOR_CONSUMERS.get(transactionId);
    }

    public static boolean existTransactionConsumer(String transactionId) {
        return EXECUTOR_CONSUMERS.containsKey(transactionId);
    }

    public static void removeTransactionConsumer(String transactionId) {
        EXECUTOR_CONSUMERS.remove(transactionId);
    }
}

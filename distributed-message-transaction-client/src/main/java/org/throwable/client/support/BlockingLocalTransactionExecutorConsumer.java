package org.throwable.client.support;

import org.throwable.client.common.LocalTransactionStatusEnum;
import org.throwable.client.exception.LocalTransactionExecutionException;
import org.throwable.client.exception.LocalTransactionInterruptedException;
import org.throwable.client.exception.LocalTransactionTimeoutException;
import org.throwable.client.exception.LocalTransactionUnknownExecutionException;
import org.throwable.client.model.ExecutorConsumerParameter;
import org.throwable.client.model.MessageTransactionResult;

import java.util.concurrent.*;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 15:20
 */
public class BlockingLocalTransactionExecutorConsumer {

    private final static int DEFAULT_EXECUTOR_SIZE = 1;
    private final String transactionId;
    private final String businessSign;
    private final int triggerTransactionTimeoutSecond;
    private final int executeTransactionTimeoutSecond;
    private final BlockingQueue<LocalTransactionExecutorAdapter> localTransactionExecutors;
    private final ExecutorService executorService;

    public BlockingLocalTransactionExecutorConsumer(ExecutorConsumerParameter parameter) {
        this.transactionId = parameter.getTransactionId();
        this.businessSign = parameter.getBusinessSign();
        this.triggerTransactionTimeoutSecond = parameter.getTriggerTransactionTimeoutSecond();
        this.executeTransactionTimeoutSecond = parameter.getExecuteTransactionTimeoutSecond();
        this.executorService = parameter.getExecutorService();
        this.localTransactionExecutors = new LinkedBlockingQueue<>(DEFAULT_EXECUTOR_SIZE);
    }

    public void addLocalTransactionExecutor(LocalTransactionExecutorAdapter transactionExecutor) {
        try {
            this.localTransactionExecutors.put(transactionExecutor);
        } catch (InterruptedException e) {
            throw new LocalTransactionInterruptedException("Process addLocalTransactionExecutor interrupted", e);
        }
    }

    public MessageTransactionResult processLocalTransactionExecutor() {
        return processLocalTransactionExecutor(triggerTransactionTimeoutSecond, executeTransactionTimeoutSecond, TimeUnit.SECONDS);
    }

    private MessageTransactionResult processLocalTransactionExecutor(int triggerTransactionTimeoutSecond,
                                                                     int executeTransactionTimeoutSecond,
                                                                     TimeUnit unit) {
        LocalTransactionExecutorAdapter executor;
        try {
            executor = localTransactionExecutors.poll(triggerTransactionTimeoutSecond, unit);
            if (null == executor) {
                throw new LocalTransactionTimeoutException(String.format("Trigger transaction timeout,transactionId:%s,businessSign:%s", transactionId, businessSign));
            }
            final CompletableFuture<LocalTransactionStatusEnum> future = new CompletableFuture<>();
            executorService.submit((Callable<Void>) () -> {
                try {
                    future.complete(executor.doInLocalTransaction());
                } catch (Exception e) {
                    //理论上不会走进这里的逻辑
                    future.completeExceptionally(new LocalTransactionExecutionException(e));
                }
                return null;
            });
            LocalTransactionStatusEnum localTransactionStatus = future.get(executeTransactionTimeoutSecond, TimeUnit.SECONDS);
            MessageTransactionResult result = new MessageTransactionResult();
            result.setBusinessSign(businessSign);
            result.setTransactionId(transactionId);
            result.setTransactionStatus(localTransactionStatus);
            return result;
        } catch (Exception e) {
            handleTransactionException(e);
        }
        throw new LocalTransactionExecutionException(String.format("Process processLocalTransactionExecutor failed,transactionId:%s,businessSign:%s", transactionId, businessSign));
    }

    private void handleTransactionException(Exception e) {
        if (e instanceof InterruptedException) {
            throw new LocalTransactionInterruptedException(String.format("Process processLocalTransactionExecutor interrupted,transactionId:%s,businessSign:%s", transactionId, businessSign), e);
        } else if (e instanceof ExecutionException) {
            throw new LocalTransactionExecutionException(String.format("Process processLocalTransactionExecutor failed,transactionId:%s,businessSign:%s", transactionId, businessSign), e);
        } else if (e instanceof TimeoutException) {
            throw new LocalTransactionTimeoutException(String.format("Process processLocalTransactionExecutor timeout,transactionId:%s,businessSign:%s", transactionId, businessSign), e);
        } else if (e instanceof LocalTransactionTimeoutException) {
            throw (LocalTransactionTimeoutException) e;
        } else if (e instanceof LocalTransactionExecutionException) {
            throw (LocalTransactionExecutionException) e;
        } else {
            throw new LocalTransactionUnknownExecutionException(String.format("Process processLocalTransactionExecutor encounters an unknown exception,transactionId:%s,businessSign:%s", transactionId, businessSign), e);
        }
    }
}

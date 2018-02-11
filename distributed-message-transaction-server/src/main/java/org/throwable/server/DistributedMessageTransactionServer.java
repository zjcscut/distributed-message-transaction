package org.throwable.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 14:40
 */
@SpringBootApplication
public class DistributedMessageTransactionServer {

    public static void main(String[] args) {
        SpringApplication.run(DistributedMessageTransactionServer.class, args);
    }
}

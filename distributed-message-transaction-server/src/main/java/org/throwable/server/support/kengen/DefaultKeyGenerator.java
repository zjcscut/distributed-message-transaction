package org.throwable.server.support.kengen;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.throwable.server.configuration.BaseServerProperties;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/5 15:31
 */
public class DefaultKeyGenerator implements KeyGenerator, InitializingBean {

    public static final long EPOCH;

    private static final long SEQUENCE_BITS = 12L;

    private static final long WORKER_ID_BITS = 10L;

    private static final long SEQUENCE_MASK = (1L << SEQUENCE_BITS) - 1L;

    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;

    private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;

    private static final long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;

    private static final SystemClock CLOCK = new SystemClock();

    private long workerId;

    private long sequence;

    private long lastTime;

    @Autowired
    private BaseServerProperties baseServerProperties;

    static {
        //纪元起始日期:2018-2-5 12:00:00
        LocalDateTime start = LocalDateTime.of(2018, 2, 5, 12, 0, 0);
        EPOCH = start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.workerId = baseServerProperties.getKeygenWorkerId();
    }

    @Override
    public synchronized long generateKey() {
        long time = CLOCK.millis();
        Assert.isTrue(lastTime <= time, String.format("Clock is moving backwards, last time is %d milliseconds, current time is %d milliseconds", lastTime, time));
        if (lastTime == time) {
            if (0L == (++sequence & SEQUENCE_MASK)) {
                time = waitUntilNextTime(time);
            }
        } else {
            sequence = 0;
        }
        lastTime = time;
        return ((time - EPOCH) << TIMESTAMP_LEFT_SHIFT_BITS) | (workerId << WORKER_ID_LEFT_SHIFT_BITS) | sequence;
    }

    private long waitUntilNextTime(final long lastTime) {
        long time = CLOCK.millis();
        while (time <= lastTime) {
            time = CLOCK.millis();
        }
        return time;
    }

    private static class SystemClock {

        private long millis() {
            return System.currentTimeMillis();
        }
    }
}

package com.alipay.sofa.serverless.arklet.core.command.record;

import com.alipay.sofa.serverless.arklet.core.common.log.ArkletLogger;
import com.alipay.sofa.serverless.arklet.core.common.log.ArkletLoggerFactory;
import lombok.Getter;
import lombok.Setter;

import static com.alipay.sofa.serverless.arklet.core.command.record.ProcessRecord.Status.EXECUTING;
import static com.alipay.sofa.serverless.arklet.core.command.record.ProcessRecord.Status.FAILED;
import static com.alipay.sofa.serverless.arklet.core.command.record.ProcessRecord.Status.INITIALIZED;
import static com.alipay.sofa.serverless.arklet.core.command.record.ProcessRecord.Status.SUCCEEDED;

/**
 * @author: yuanyuan
 * @date: 2023/8/31 3:27 下午
 */
@Getter
@Setter
public class ProcessRecord {

    private static final ArkletLogger LOGGER = ArkletLoggerFactory.getDefaultLogger();

    private String                  requestId;

    private String                  threadName;

    private Status                  status;

    private Throwable               throwable;

    private String             errorCode;

    private String             message;

    private long               startTimestamp;

    private long               endTimestamp;

    private long               elapsedTime;


    public enum Status {

        INITIALIZED("INITIALIZED"),

        EXECUTING("EXECUTING"),

        SUCCEEDED("SUCCEEDED"),

        FAILED("FAILED");

        private String             name;

        Status(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public void markFinishTime() {
        long end = System.currentTimeMillis();
        setEndTimestamp(end);
        setElapsedTime(end - startTimestamp);
    }

    public boolean finished() {
        return SUCCEEDED.equals(getStatus()) || FAILED.equals(getStatus());
    }

    public void start() {
        if (INITIALIZED.equals(getStatus())) {
            setStatus(EXECUTING);
            LOGGER.info("Command execution status change: INIT -> EXECUTING");
        }
    }

    public void success() {
        if (EXECUTING.equals(getStatus())) {
            setStatus(SUCCEEDED);
            LOGGER.info("Command execution status change: EXECUTING -> SUCCESS");
        }
    }

    public void fail() {
        if (EXECUTING.equals(getStatus())) {
            setStatus(FAILED);
            LOGGER.info("Command execution status change: EXECUTING -> FAIL");
        }
    }

    public void fail(String message) {
        fail();
        setMessage(message);
    }

    public void fail(String message, Throwable throwable) {
        fail();
        setMessage(message);
        this.throwable = throwable;
    }

}

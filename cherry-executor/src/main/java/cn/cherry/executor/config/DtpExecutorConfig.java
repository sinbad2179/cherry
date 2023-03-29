package cn.cherry.executor.config;

import cn.hutool.core.util.RuntimeUtil;
import com.dtp.common.em.QueueTypeEnum;
import com.dtp.common.em.RejectedTypeEnum;
import com.dtp.core.support.ThreadPoolBuilder;
import com.dtp.core.thread.DtpExecutor;

import java.util.concurrent.TimeUnit;

/**
 * @author: sinbad.cheng
 * @time: 2023/3/29 18:27
 * @description： 线程池的配置
 */
public class DtpExecutorConfig {

    /**
     * 线程池名称前缀
     */
    private static final String PREFIX = "cherry.";

    /**
     * 阻塞队列大小
     */
    public static final Integer COMMON_QUEUE_SIZE = 128;


    /**
     * 获取线程池配置
     *
     * @param groupId
     * @return
     */
    public static DtpExecutor getExecutor(String groupId) {
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName(PREFIX + groupId)
                .corePoolSize(RuntimeUtil.getProcessorCount() + 1)
                .maximumPoolSize(RuntimeUtil.getProcessorCount() + 1)
                .keepAliveTime(60)
                .timeUnit(TimeUnit.SECONDS)
                .rejectedExecutionHandler(RejectedTypeEnum.CALLER_RUNS_POLICY.getName())
                .allowCoreThreadTimeOut(false)
                .workQueue(QueueTypeEnum.VARIABLE_LINKED_BLOCKING_QUEUE.getName(), COMMON_QUEUE_SIZE, false)
                .buildDynamic();
    }
}

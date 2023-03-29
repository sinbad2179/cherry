package cn.cherry.executor.config;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: sinbad.cheng
 * @time: 2023/3/29 20:57
 * @description： 优雅停止线程池
 */
@Component
@Slf4j
public class ThreadExecutorShutdownDefinition implements ApplicationListener<ContextClosedEvent> {

    /**
     * 线程池组
     */
    private final List<ExecutorService> POOLS = Collections.synchronizedList(new ArrayList<>(12));

    /**
     * 线程中的任务在接收到应用关闭信号量后最多等待多久就强制终止
     * <p>
     * (其实就是给剩余任务预留的时间， 到时间后线程池必须销毁)
     */
    private final long AWAIT_TERMINATION = 20;


    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("容器关闭前处理线程池优雅关闭开始, 当前要处理的线程池数量为: {} >>>>>>>>>>>>>>>>", POOLS.size());
        if (CollectionUtil.isEmpty(POOLS)) {
            return;
        }
        for (ExecutorService pool : POOLS) {
            // 停止
            pool.shutdown();
            try {
                if (!pool.awaitTermination(AWAIT_TERMINATION, TimeUnit.SECONDS)) {
                    if (log.isWarnEnabled()) {
                        log.warn("Timed out while waiting for executor [{}] to terminate", pool);
                    }
                }
            } catch (InterruptedException e) {
                if (log.isWarnEnabled()) {
                    log.warn("Timed out while waiting for executor [{}] to terminate", pool);
                }
                Thread.currentThread().interrupt();
            }
        }
    }


    /**
     * 注册到线程池组中
     *
     * @param executorService
     */
    public void registryExecutor(ExecutorService executorService) {
        POOLS.add(executorService);
    }

}

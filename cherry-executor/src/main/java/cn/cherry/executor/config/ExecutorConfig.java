package cn.cherry.executor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Configuration
public class ExecutorConfig {

    @Bean(value = "threadPoolExecutor", destroyMethod = "shutdown")
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(
                        getProcessorCount() + 1,
                        getProcessorCount() + 1,
                        60, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(100));
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }





    /**
     * 获得JVM可用的处理器数量（一般为CPU核心数）
     *
     * @return 可用的处理器数量
     */
    public static int getProcessorCount() {
        return Runtime.getRuntime().availableProcessors();
    }

}

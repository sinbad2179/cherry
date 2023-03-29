package cn.cherry.executor.config;

import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: sinbad.cheng
 * @time: 2023/3/29 20:55
 * @description：
 */
@Component
public class ThreadPoolUtils {

    @Autowired
    private ThreadExecutorShutdownDefinition shutdownDefinition;

    /**
     * 来源
     */
    private static final String SOURCE = "cherry";

    /**
     * 将当前线程池加入到动态线程池中
     * <p>
     * 将线程池注册到Spring中管理，并优雅关闭
     *
     * @param dtpExecutor
     */
    public void register(DtpExecutor dtpExecutor) {
        DtpRegistry.registerDtp(dtpExecutor, SOURCE);
        shutdownDefinition.registryExecutor(dtpExecutor);
    }


}

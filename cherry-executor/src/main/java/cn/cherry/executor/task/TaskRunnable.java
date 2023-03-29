package cn.cherry.executor.task;

import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author: sinbad.cheng
 * @time: 2023/3/29 17:41
 * @description： 执行任务线程
 * <p>
 * ps:确保线程安全
 */
@Data
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskRunnable implements Runnable {


    /**
     * 业务值
     */
    private Object object;


    @Override
    public void run() {
        System.out.println("线程任务开始执行了...");
    }
}

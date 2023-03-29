package cn.cherry.executor.service;

import cn.cherry.executor.task.TaskHolder;
import cn.cherry.executor.task.TaskRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: sinbad.cheng
 * @time: 2023/3/29 17:36
 * @description： 动态线程池服务类
 */
@Component
public class DynamicThreadPoolService {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private TaskHolder taskHolder;

    /**
     * 测试动态线程池
     *
     * @param taskRunnables
     */
    public void testDynamicThreadPool(List<Object> taskRunnables) {
        // ToDo
        String groupId = "";
        for (Object obj : taskRunnables) {
            TaskRunnable runnable = context.getBean(TaskRunnable.class);
            runnable.setObject(obj);

            taskHolder.getExecutor(groupId).execute(runnable);
        }
    }

}

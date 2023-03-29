package cn.cherry.executor.task;

import cn.cherry.executor.ChannelTypeEnum;
import cn.cherry.executor.config.DtpExecutorConfig;
import cn.cherry.executor.config.ThreadPoolUtils;
import com.dtp.core.thread.DtpExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @author: sinbad.cheng
 * @time: 2023/3/29 17:50
 * @description：
 */
@Component
public class TaskHolder {

    @Autowired
    private ThreadPoolUtils threadPoolUtils;

    /**
     * 线程池任务组
     */
    private Map<String, ExecutorService> taskHolderGroup = new HashMap<>(32);


    /**
     * 线程池初始化，给各个渠道初始化一个线程池
     */
    @PostConstruct
    public void init() {
        for (String groupId : getGroups()) {
            DtpExecutor dtpExecutor = DtpExecutorConfig.getExecutor(groupId);
            // 加入到动态线程池
            threadPoolUtils.register(dtpExecutor);

            taskHolderGroup.put(groupId, dtpExecutor);

        }
    }


    /**
     * 获取线程池
     *
     * @param groupId
     * @return
     */
    public ExecutorService getExecutor(String groupId) {
        return taskHolderGroup.get(groupId);
    }


    /**
     * 获取线程池唯一ID集合
     *
     * @return
     */
    public List<String> getGroups() {
        return Arrays.stream(ChannelTypeEnum.values()).map(ChannelTypeEnum::getUniqueId).collect(Collectors.toList());
    }

}

package cn.cherry.excel.service;

/**
 * @author: sinbad.cheng
 * @time: 2023/4/10 22:24
 * @description： 处理导出任务类
 */
public interface TaskHandlerService {

    /**
     * 任务校验
     *
     * @param moduleType
     * @param userId
     * @return
     */
    boolean checkTaskIsExist(String moduleType, Integer userId);
}

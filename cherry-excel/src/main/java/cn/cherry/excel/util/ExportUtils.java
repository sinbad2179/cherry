package cn.cherry.excel.util;

import cn.cherry.core.exception.ServiceException;
import cn.cherry.core.model.Result;
import cn.cherry.excel.enums.ExportEnum;
import cn.cherry.excel.model.BasicExportModel;
import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: sinbad.cheng
 * @time: 2023/4/2 18:18
 * @description：
 */
@Component
@Log4j2
public class ExportUtils {


    /**
     * 创建异步任务
     *
     * @param exportModel 导出执行参数
     * @param exportEnum  导出条件
     * @param <T>
     * @return
     */
    public static <T extends BasicExportModel> Result<Void> createAsyncTask(T exportModel, ExportEnum exportEnum) {

        exportModel.setUserInfo(null);

        Map<String, String> conditionMap = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        paramMap.put("moduleType", exportEnum.getModuleType());
        paramMap.put("userInfo", exportModel.getUserInfo());
        paramMap.put("condition", JSON.toJSONString(exportModel));
        // 查询是否存在执行任务 ToDo 按照paramMap条件 查询导出任务表&校验
        if (checkIsExistTask(paramMap)) {
            throw new ServiceException("存在重复任务");
        }

        // 创建导出任务
        long task = createTask(paramMap);
        if (Objects.isNull(task)) {
            throw new ServiceException("创建导出任务失败");
        }
        // 执行导出
        exportModel.setJobId(task);
        execute("");


        return Result.success();

    }


    /**
     * 开始导出
     *
     * @param args
     */
    private static void execute(String args) {
        log.info("开始导出~");
        try {
            // 获取导出任务集合

            // 遍历导出任务


        } catch (Exception e) {
            log.error("err info:{}", JSON.toJSONString(e));
        }
    }


    /**
     * 生成导出任务
     *
     * @param paramMap
     * @return 任务ID
     */
    private static long createTask(Map<String, Object> paramMap) {
        long jobId = 0;

        return jobId;
    }


    /**
     * 校验是否存在任务
     *
     * @param paramMap 导出条件
     * @return
     */
    private static boolean checkIsExistTask(Map<String, Object> paramMap) {
        return false;
    }
}

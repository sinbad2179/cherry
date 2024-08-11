//package cn.cherry.excel.service.impl;
//
//import cn.cherry.excel.enums.TaskStatusEnum;
//import cn.cherry.excel.mapper.TaskHandlerMapper;
//import cn.cherry.excel.service.TaskHandlerService;
//import cn.cherry.excel.service.task.ExportTaskModel;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.apache.commons.collections4.CollectionUtils;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * @author: sinbad.cheng
// * @time: 2023/4/10 22:26
// * @description：
// */
//@Service
//public class TaskHandlerServiceImpl extends ServiceImpl<TaskHandlerMapper, ExportTaskModel> implements TaskHandlerService {
//
//    @Override
//    public boolean checkTaskIsExist(String moduleType, Integer userId) {
//        List<ExportTaskModel> exportTaskModels = baseMapper.selectList(new LambdaQueryWrapper<ExportTaskModel>()
//                .eq(ExportTaskModel::getCreateCode, userId)
//                .eq(ExportTaskModel::getModuleType, moduleType)
//                .eq(ExportTaskModel::getStatusType, TaskStatusEnum.DOING.getCode())
//        );
//        return CollectionUtils.isNotEmpty(exportTaskModels);
//    }
//}

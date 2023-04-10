package cn.cherry.excel.service.impl;

import cn.cherry.excel.dto.TestExportDTO;
import cn.cherry.excel.dto.TestExportRequestDTO;
import cn.cherry.excel.enums.ExportEnum;
import cn.cherry.excel.service.TestExportService;
import cn.cherry.excel.util.ExportUtils;
import org.springframework.stereotype.Service;

/**
 * @author: sinbad.cheng
 * @time: 2023/4/10 22:00
 * @description： 导出测试
 */
@Service
public class TestExportServiceImpl implements TestExportService {

    @Override
    public void export(TestExportRequestDTO dto) {
        TestExportDTO testExportDTO = new TestExportDTO();
        testExportDTO.setName(dto.getName());
        testExportDTO.setStatus(dto.getStatus());
        testExportDTO.setUserInfo(null);
        // 执行异步导出
        ExportUtils.createAsyncTask(testExportDTO, ExportEnum.of(dto.getModuleType()));
    }
}

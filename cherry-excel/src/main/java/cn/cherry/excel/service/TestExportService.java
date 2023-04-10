package cn.cherry.excel.service;

import cn.cherry.excel.dto.TestExportRequestDTO;

/**
 * @author: sinbad.cheng
 * @time: 2023/4/10 21:54
 * @description：
 */
public interface TestExportService {

    /**
     * 导出测试方法
     *
     * @param testExportRequestDTO
     */
    void export(TestExportRequestDTO testExportRequestDTO);
}

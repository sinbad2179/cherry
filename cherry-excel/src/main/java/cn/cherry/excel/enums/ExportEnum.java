package cn.cherry.excel.enums;

import cn.cherry.excel.dto.TestExportDTO;
import cn.cherry.excel.model.BasicExportModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author: sinbad.cheng
 * @time: 2023/4/2 18:49
 * @description： 导出枚举
 */
@Getter
@AllArgsConstructor
public enum ExportEnum {

    // 导出方法测试
    TEST_EXPORT("testExport", "测试导出", "asyncTestExport", TestExportDTO.class),

    ;

    /**
     * 模块类型
     */
    private String moduleType;


    /**
     * 模块NAME
     */
    private String moduleName;

    /**
     * 执行方法
     */
    private String method;

    /**
     * 请求参数
     */
    private Class<? extends BasicExportModel> requestModel;


    /**
     * 匹配具体导出模块
     *
     * @param reqType
     * @return
     */
    public static ExportEnum of(String reqType) {
        return Arrays.stream(ExportEnum.values()).filter(v -> StringUtils.equals(v.getModuleType(), reqType)).findFirst().orElse(null);
    }

}

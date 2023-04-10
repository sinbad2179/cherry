package cn.cherry.excel.dto;

import cn.cherry.excel.model.BasicExportModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author: sinbad.cheng
 * @time: 2023/4/2 18:54
 * @description： 测试导出请求Model
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TestExportDTO extends BasicExportModel implements Serializable {
    private static final long serialVersionUID = 4079273700783960236L;

    /**
     * 查询集合id
     */
    private String name;

    /**
     * 状态 0成功 1失败
     */
    private Integer status;
}

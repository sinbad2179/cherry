package cn.cherry.excel.dto;

import cn.cherry.excel.model.basic.RequestBasicModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: sinbad.cheng
 * @time: 2023/4/10 21:57
 * @description： 查询XXX报表记录
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TestExportRequestDTO extends RequestBasicModel {
    private static final long serialVersionUID = 479001187874792430L;

    private String name;


    /**
     * 状态 0成功 1失败
     */
    private Integer status;

    private String startTime;
    private String endTime;
}

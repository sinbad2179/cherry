package cn.cherry.excel.model;

import cn.cherry.core.model.UserInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: sinbad.cheng
 * @time: 2023/4/2 18:19
 * @description： 导出模型超类
 */
@Data
public class BasicExportModel implements Serializable {
    private static final long serialVersionUID = -1619276639312998455L;

    /**
     * 任务ID
     */
    private Long jobId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 模块类型
     */
    private String moduleType;


    /**
     * 模块NAME
     */
    private String moduleName;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * sheet名称
     */
    private String sheetName;

    /**
     * 最大记录
     */
    private Integer maxTotal;


    /**
     * 用户信息
     */
    private UserInfo userInfo;

    /**
     * 导出列
     */
    private ColumnNode columnNode;


}

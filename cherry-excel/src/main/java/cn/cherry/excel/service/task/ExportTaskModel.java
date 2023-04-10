package cn.cherry.excel.service.task;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: sinbad.cheng
 * @time: 2023/4/10 22:29
 * @description： 导出任务实体
 */
@Data
public class ExportTaskModel implements Serializable {

    private static final long serialVersionUID = -7289969708567921092L;


    /**
     * 任务名
     */
    private String jobName;

    /**
     * 模块类型
     */
    private String moduleType;

    /**
     * 模块NAME
     */
    private String moduleName;

    /**
     * 任务条件
     */
    private String condition;

    /**
     * 状态类型:0. 排队中 1. 进行中 2. 成功
     */
    private Integer statusType;

    /**
     * 文件路径
     */
    private String fileUrl;


    /**
     * 创建人CODE
     */
    private String createCode;

    /**
     * 创建人名称
     */
    private String createByName;

    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 更新人CODE
     */
    private String updateCode;

    /**
     * 最后更新人名称
     */
    private String updateByName;

    /**
     * 最后更新时间
     */
    private Date updateTime;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 排序
     */
    private Integer sort;

    private Date fileGenerateTime;

    /**
     * 异常备注
     */
    private String statusRemark;


}

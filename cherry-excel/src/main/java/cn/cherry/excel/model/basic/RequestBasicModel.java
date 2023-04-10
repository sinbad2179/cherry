package cn.cherry.excel.model.basic;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: sinbad.cheng
 * @time: 2023/4/10 22:17
 * @description： 基础请求超类
 */
@Data
public class RequestBasicModel implements Serializable {

    private static final long serialVersionUID = -8308114968767541450L;

    /**
     * 导出模块类型，导出具体是哪个模块由请求时指定
     */
    private String moduleType;
}

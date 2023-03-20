package cn.cherry.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * 响应结果
 *
 * @author <a href="kafka@88.com">刘伟(Marvin)</a>
 * @since 2022-01-14
 */
@Data
@ToString
@AllArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    /**
     * 链路ID
     */
    private String traceId;

    /**
     * 填充参数
     */
    private transient Object[] args;


}
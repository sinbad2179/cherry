package cn.cherry.core.model;

import cn.cherry.core.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * 响应结果
 *
 * @author sinbad
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

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // =====TODO

    /**
     * 失败返回
     */
    public static <T> Result<T> failure(BaseErrorCode errorCode, Object... args) {
        return failure(errorCode.getCode(), errorCode.getMsg(), args);
    }


    /**
     * 失败返回
     */
    public static <T> Result<T> failure(int code, String msg, Object... args) {
        return new Result<T>(code, msg).args(args);
    }

    private Result<T> args(Object[] args) {
        this.args = args;
        return this;
    }
}
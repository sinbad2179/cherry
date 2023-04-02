package cn.cherry.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: sinbad.cheng
 * @time: 2023/2/26 17:51
 * @description： 服务异常类
 */
@Getter
@AllArgsConstructor
public enum ServiceErrorCode implements BaseErrorCode {


    ARGS_IS_NULL(10001, "参数不能为空！"),


    // ========================== 服务端错误 ==========================
    /**
     * 成功：1
     */
    SUCCESS(0, "请求成功"),

    FAILURE(1, "请求失败"),

    /**
     * 系统内部错误：500
     */
    SERVER_ERROR(500, "服务器繁忙~"),

    ;


    /**
     * 异常code
     */
    private final Integer code;

    /**
     * 异常信息
     */
    private final String errMsg;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return errMsg;
    }
}

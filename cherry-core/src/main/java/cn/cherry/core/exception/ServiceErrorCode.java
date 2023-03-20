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
public enum ServiceErrorCode implements BaseErrorCode<ServiceException> {


    ARGS_IS_NULL(10001, "参数不能为空！"),

    ;


    /**
     * 异常code
     */
    private final Integer code;

    /**
     * 异常信息
     */
    private final String errMsg;


}

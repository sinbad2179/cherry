package cn.cherry.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: sinbad.cheng
 * @time: 2023/2/22 20:34
 * @description： 异常
 */
@Getter
@AllArgsConstructor
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer code;


    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}

package cn.cherry.core.exception;

import lombok.Getter;

/**
 * @author: sinbad.cheng
 * @time: 2023/3/21 20:20
 * @description：
 */
@Getter
public class BusinessException extends ServiceException {


    public BusinessException(Integer code, Object[] args) {
        super(code, args);
    }
}

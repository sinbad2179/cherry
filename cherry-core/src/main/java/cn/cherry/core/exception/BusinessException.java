package cn.cherry.core.exception;

import lombok.Getter;

/**
 * @author: sinbad.cheng
 * @time: 2023/3/21 20:20
 * @description：
 */
@Getter
public class BusinessException extends ServiceException {


    private static final long serialVersionUID = 2331186443660148375L
            ;

    public BusinessException(Integer code, Object[] args) {
        super(code, args);
    }
}

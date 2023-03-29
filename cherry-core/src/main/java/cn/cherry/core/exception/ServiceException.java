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
public class ServiceException extends RuntimeException implements BaseErrorCode {

    private static final long serialVersionUID = 1L;

    private final Integer code;


    /**
     * 消息格式参数
     */
    private final Object[] args;

    public ServiceException(final BaseErrorCode errorCode, Object... args) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.args = args;
    }

    public ServiceException(int code, String msg, Object... args) {
        super(msg);
        this.code = code;
        this.args = args;
    }

    public ServiceException(String msg, Object... args) {
        super(msg);
        //TODO
        this.code = ServiceErrorCode.SERVER_ERROR.getCode();
        this.args = args;
    }

    public ServiceException(BaseErrorCode errorCode, String message, Object... args) {
        super(message);
        this.code = errorCode.getCode();
        this.args = args;
    }

    public ServiceException(int code, String message, Throwable cause, Object... args) {
        super(message, cause);
        this.code = code;
        this.args = args;
    }

    public ServiceException(BaseErrorCode errorCode, Throwable cause, Object... args) {
        this(errorCode.getCode(), errorCode.getMsg(), cause, args);
    }


    @Override
    public String getMsg() {
        return null;
    }

    public int getCode() {
        return code;
    }
}

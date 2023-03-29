package cn.cherry.core.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * @author: sinbad.cheng
 * @time: 2023/3/29 17:08
 * @description：
 */
@Deprecated
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 6951461176023604144L;

    private String errorCode;
    private String errorMsg;
    private String returnMsg;
    private String lang;
    private Integer printLog;

    public BaseException() {
        this.printLog = 1;
    }

    public BaseException(String errorCode, String errorMsg) {
        this.printLog = 1;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BaseException(String errorCode, String errorMsg, Integer printLog) {
        this(errorCode, errorMsg);
        this.printLog = printLog;
    }

    public BaseException(String errorCode, String errorMsg, Throwable t) {
        super(t);
        this.printLog = 1;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BaseException(String errorCode, String errorMsg, Integer printLog, Throwable t) {
        this(errorCode, errorMsg, t);
        this.printLog = printLog;
    }

    public BaseException(String errorCode, String errorMsg, String returnMsg) {
        this.printLog = 1;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.returnMsg = returnMsg;
    }

    public BaseException(String errorCode, String errorMsg, String returnMsg, Integer printLog) {
        this(errorCode, errorMsg, returnMsg);
        this.printLog = printLog;
    }

    public BaseException(String errorCode, String errorMsg, String returnMsg, Throwable t) {
        super(t);
        this.printLog = 1;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.returnMsg = returnMsg;
    }

    public BaseException(String errorCode, String errorMsg, String returnMsg, Integer printLog, Throwable t) {
        this(errorCode, errorMsg, returnMsg, printLog);
        this.printLog = printLog;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getReturnMsg() {
        return StringUtils.isBlank(this.returnMsg) ? this.errorMsg : this.returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public Integer getPrintLog() {
        return this.printLog;
    }

    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setPrintLog(Integer printLog) {
        this.printLog = printLog;
    }

    public String toString() {
        return super.toString() + " [errorCode = " + this.errorCode + "，errorMsg=" + this.errorMsg + "]";
    }

    public String getExceptionName() {
        return this.errorCode + "-" + this.errorMsg;
    }
}

package cn.cherry.core.exception;

/**
 * @author: sinbad.cheng
 * @time: 2023/2/26 17:42
 * @description： 异常码 ToDo
 */
public interface BaseErrorCode {

    /**
     * 获取编码
     */
    int getCode();

    /**
     * 获取粗欧文消息
     */
    String getMsg();

}

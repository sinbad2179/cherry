package cn.cherry.core.exception.enums;

import cn.cherry.core.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author :  sinbad.cheng
 * @since :  2024-07-18 10:54
 */
@AllArgsConstructor
@Getter
@ToString
public enum CommonErrorCode implements BaseErrorCode {

    // ========================== 认证/授权（14000） ==========================
    /**
     * 签名校验失败：14240
     */
    SIGNATURE_VERIFICATION_FAILED(14240, "签名校验失败"),


    // ========================== 认证/授权（14000） ==========================
    /**
     * 没有访问权限：14000
     */
    FORBIDDEN(14000, "没有访问权限"),
    /**
     * 请求未授权：14100
     */
    UNAUTHORIZED(14100, "请求未授权"),

    /**
     * 登录已失效，请重新登录：14280
     */
    LOGIN_EXPIRED(14280, "登录已失效，请重新登录"),
    ;



    /**
     * 状态码
     */
    final int code;

    /**
     * 消息
     */
    final String msg;


    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}

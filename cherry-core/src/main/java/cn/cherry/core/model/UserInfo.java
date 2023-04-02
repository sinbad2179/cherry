package cn.cherry.core.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: sinbad.cheng
 * @time: 2023/4/2 18:22
 * @description： 用户信息
 */
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -3649578516746949071L;

    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
}

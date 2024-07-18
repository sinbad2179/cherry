package cn.cherry.authenticate.userdetails;

import cn.cherry.core.util.IdUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Optional;

/**
 * 认证用户详情
 *
 * @author :  sinbad.cheng
 * @since :  2024-07-18 11:47
 */

public class AuthUserDetails extends User {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    /**
     * 用户ID
     */
    private final String clientId;

    /**
     * 授权ID，用于多客户端登录确定唯一
     */
    private final String authId;

    /**
     * 用户ID
     */
    private final Long userId;

    /**
     * 账号
     */
    private final String account;

    /**
     * 手机号
     */
    private final String mobile;

    /**
     * 邮箱
     */
    private final String email;

    /**
     * 名称（学员端昵称）
     */
    private final String nickName;



    public AuthUserDetails(String username,
                           String password,
                           Collection<? extends GrantedAuthority> authorities,
                           String clientId,
                           String authId,
                           Long userId,
                           String account,
                           String mobile,
                           String email,
                           String nickName) {
        super(username, password, authorities);
        this.mobile = Optional.ofNullable(mobile).orElse("");
        this.nickName = Optional.ofNullable(nickName).orElse("");
        this.email = Optional.ofNullable(email).orElse("");
        this.authId = IdUtil.uuid2();
        this.userId = userId;
        this.account = account;
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAuthId() {
        return authId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAccount() {
        return account;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public String toString() {
        return "AuthUserDetails{" +
                "clientId='" + clientId + '\'' +
                ", authId='" + authId + '\'' +
                ", userId=" + userId +
                ", account='" + account + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }

}

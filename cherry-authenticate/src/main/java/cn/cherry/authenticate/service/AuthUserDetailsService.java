package cn.cherry.authenticate.service;

import cn.cherry.authenticate.userdetails.AuthUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * 用户详情服务。
 *
 * @author :  sinbad.cheng
 * @since :  2024-07-18 11:45
 */
public class AuthUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthUserDetailsService.class);
    // TODO 注入一个用户Service，查询用户信息

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("账号密码登录查询：{}", username);
        // getUserByName();


        // TODO @SINBAD 待处理
        return new AuthUserDetails(null, null, null, null, null, null, null, null, null, null);
    }


    // TODO @SINBAD 待处理
    public void cacheUserPermissions(String clientId, OAuth2AccessToken accessToken, AuthUserDetails authUserDetails) {

    }
}

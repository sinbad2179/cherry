package cn.cherry.authenticate.token;

import cn.cherry.authenticate.service.AuthUserDetailsService;
import cn.cherry.authenticate.userdetails.AuthUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author :  sinbad.cheng
 * @see DefaultTokenServices
 * @since :  2024-07-18 12:02
 */
public class AuthTokenService extends DefaultTokenServices {
    private final TokenStore tokenStore;
    private final AuthUserDetailsService authUserDetailsService;


    public AuthTokenService(TokenStore tokenStore,
                            TokenEnhancer tokenEnhancer,
                            AuthenticationManager authenticationManager,
                            AuthUserDetailsService authUserDetailsService,
                            ClientDetailsService clientDetailsService) {
        this.tokenStore = tokenStore;
        this.authUserDetailsService = authUserDetailsService;
        // 设置令牌的持久化容器
        // 令牌持久化有多种方式，单节点服务可以存放在Session中，集群可以存放在Redis中
        // 而JWT是后端无状态、前端存储的解决方案，Token的存储由前端完成
        setTokenStore(tokenStore);
        // 令牌支持的客户端详情
        setClientDetailsService(clientDetailsService);
        // 定义令牌的额外支持
        setTokenEnhancer(tokenEnhancer);
        // 设置验证管理器，在鉴权的时候需要用到
        setAuthenticationManager(authenticationManager);
        // access_token有效期，单位：秒
        setAccessTokenValiditySeconds(8000);// TODO @Sinbad放在apollo
        // refresh_token的有效期，单位：秒, 默认30天
        // 这决定了客户端选择“记住当前登录用户”的最长时效，即失效前都不用再请求用户赋权了
        setRefreshTokenValiditySeconds(4000);// TODO @Sinbad放在apollo
        // 是否支持refresh_token，默认false
        setSupportRefreshToken(true);
        // 是否复用refresh_token，默认为true
        // 如果为false，则每次请求刷新都会删除旧的refresh_token，创建新的refresh_token
        setReuseRefreshToken(false);
    }

    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        OAuth2AccessToken accessToken = super.createAccessToken(authentication);

        // 缓存用户和权限信息
        if (accessToken != null) {
            String clientId = authentication.getOAuth2Request().getClientId();
            AuthUserDetails authUserDetails = (AuthUserDetails) authentication
                    .getUserAuthentication().getPrincipal();
            authUserDetailsService.cacheUserPermissions(clientId, accessToken, authUserDetails);
        }
        return accessToken;
    }

    @Override
    public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest) throws AuthenticationException {
        OAuth2AccessToken accessToken = super.refreshAccessToken(refreshTokenValue, tokenRequest);
        if (accessToken != null) {
            String clientId = tokenRequest.getClientId();
            OAuth2Authentication authentication = tokenStore.readAuthentication(accessToken);

            // 缓存用户和权限信息
            AuthUserDetails authUserDetails = (AuthUserDetails) authentication
                    .getUserAuthentication().getPrincipal();
            authUserDetailsService.cacheUserPermissions(clientId, accessToken, authUserDetails);
        }
        return accessToken;
    }
}

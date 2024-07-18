package cn.cherry.authenticate.config;

import cn.cherry.authenticate.service.AuthUserDetailsService;
import cn.cherry.authenticate.token.AuthTokenService;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 认证服务配置
 * <p>
 * AuthorizationServerConfigurerAdapter 是一个用于配置 Spring Security OAuth2 授权服务器的适配器类。
 * 通过继承这个类并重写其方法，可以方便地配置 OAuth2 客户端、授权服务器的端点和安全性。这个配置类对于实现 OAuth2 授权服务器至关重要
 *
 * @author :  sinbad.cheng
 * @since :  2024-07-18 11:33
 */
@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties({AuthorizationServerProperties.class})
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    final TokenStore tokenStore;
    final AuthTokenService authTokenService;
    final ClientDetailsService clientDetailsService;
    final AuthenticationManager authenticationManager;
    final AuthUserDetailsService authUserDetailsService;


    public AuthorizationServerConfiguration(TokenStore tokenStore,
                                            AuthTokenService authTokenService,
                                            ClientDetailsService clientDetailsService,
                                            AuthenticationManager authenticationManager,
                                            AuthUserDetailsService authUserDetailsService) {
        this.tokenStore = tokenStore;
        this.authTokenService = authTokenService;
        this.clientDetailsService = clientDetailsService;
        this.authenticationManager = authenticationManager;
        this.authUserDetailsService = authUserDetailsService;
    }

    /**
     * 配置了令牌端点的访问权限和客户端的认证方式。
     * <p>
     * * 这些端点的默认访问规则原本是：
     * * 1. 端点开启了HTTP Basic Authentication，通过allowFormAuthenticationForClients()关闭，即允许通过表单来验证
     * * 2. 端点的访问均为denyAll()，可以在这里通过SpringEL表达式来改变为permitAll()
     *
     * @param security a fluent configurer for security features
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 允许表单认证请求
        security.allowFormAuthenticationForClients();
    }

    /**
     * 配置了客户端的详细信息，包括客户端 ID、客户端密钥、授权类型、作用域和重定向 URI
     *
     * @param clients the client details configurer
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);// TODO 暂未做接口扩展
    }


    /**
     * 配置了令牌存储、认证管理器和用户详情服务。
     * <p>
     * Spring Security OAuth2会根据配置的认证服务、用户详情服务、令牌服务自动生成以下端点：
     * * /oauth/authorize：授权端点
     * * /oauth/token：令牌端点
     * * /oauth/confirm_access：用户确认授权提交端点
     * * /oauth/error：授权服务错误信息端点
     * * /oauth/check_token：用于资源服务访问的令牌解析端点
     * * /oauth/token_key：提供公有密匙的端点，如果JWT采用的是非对称加密加密算法，则资源服务其在鉴权时就需要这个公钥来解码
     * * 如有必要，这些端点可以使用pathMapping()方法来修改它们的位置，使用prefix()方法来设置路径前缀
     *
     * @param endpoints the endpoints configurer
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(authUserDetailsService)
                .tokenGranter(null)// TODO @sinbad 待拓展
                .tokenStore(tokenStore)
                .tokenServices(authTokenService)
                // 非重复使用refresh_token
                .reuseRefreshTokens(false);
    }
}

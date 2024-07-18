package cn.cherry.authenticate.config;

import cn.cherry.authenticate.config.properties.CherrySecurityProperties;
import cn.cherry.authenticate.filter.TokenAuthenticationFilter;
import cn.cherry.authenticate.service.AuthUserDetailsService;
import cn.cherry.authenticate.token.AuthTokenService;
import cn.cherry.authenticate.token.AuthUserTokenEnhancer;
import cn.cherry.authenticate.token.CustomAuthenticationKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Arrays;

/**
 * Spring Security安全配置
 *
 * @author :  sinbad.cheng
 * @since :  2024-07-18 15:51
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final Environment environment;
    private final PasswordEncoder passwordEncoder;
    private final ClientDetailsService clientDetailsService;
    @Autowired
    private AuthUserDetailsService authUserDetailsService;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final CherrySecurityProperties securityProperties;
    private final RedisConnectionFactory redisConnectionFactory;



    public WebSecurityConfiguration(Environment environment,
                                    PasswordEncoder passwordEncoder,
                                    ClientDetailsService clientDetailsService,
                                    AccessDeniedHandler accessDeniedHandler,
                                    AuthenticationEntryPoint authenticationEntryPoint,
                                    @Qualifier("cherryClientDetailsService") CherrySecurityProperties securityProperties,
                                    RedisConnectionFactory redisConnectionFactory) {
        this.environment = environment;
        this.passwordEncoder = passwordEncoder;
        this.clientDetailsService = clientDetailsService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.securityProperties = securityProperties;
        this.redisConnectionFactory = redisConnectionFactory;
    }


    @Bean
    public TokenEnhancer tokenEnhancer() {
        AuthUserTokenEnhancer authUserTokenEnhancer = new AuthUserTokenEnhancer();
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(authUserTokenEnhancer));
        return tokenEnhancerChain;
    }

    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix("cherry.security.");
        CustomAuthenticationKeyGenerator authenticationKeyGenerator
                = new CustomAuthenticationKeyGenerator(securityProperties);
        redisTokenStore.setAuthenticationKeyGenerator(authenticationKeyGenerator);
        return redisTokenStore;
    }


    @Bean
    public AuthTokenService authTokenService() {
        try {
            return new AuthTokenService(
                    tokenStore(), tokenEnhancer(),
                    authenticationManagerBean(),
                    authUserDetailsService,
                    clientDetailsService
            );
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 需要把AuthenticationManager主动暴露出来
     * 以便在授权服务器{@link AuthorizationServerConfiguration}中可以使用它来完成用户名、密码的认证
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return authUserDetailsService;
    }

    /**
     * 预验证身份认证器
     * <p>
     * 预验证是指身份已经在其他地方（第三方）确认过
     * 预验证器的目的是将第三方身份管理系统集成到具有Spring安全性的Spring应用程序中，在本项目中，用于令牌过期后重刷新时的验证
     * 此时只要检查用户是否有停用、锁定、密码过期、账号过期等问题，如果没有，可根据令牌的刷新过期期限，重新给客户端发放访问令牌
     * <p>
     * TODO 可进行手机号身份验证、邮箱身份验证等扩展
     */
    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider =
                new PreAuthenticatedAuthenticationProvider();

        UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsByNameServiceWrapper =
                new UserDetailsByNameServiceWrapper<>(authUserDetailsService);

        preAuthenticatedAuthenticationProvider
                .setPreAuthenticatedUserDetailsService(userDetailsByNameServiceWrapper);

        return preAuthenticatedAuthenticationProvider;
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // 禁用 CSRF，因为不使用 Session
        httpSecurity.csrf().disable();
        // 禁用 HTTP 基本身份验证
        httpSecurity.httpBasic().disable();
        // 禁用请求头控制
        httpSecurity.headers()
                // 防止 iframe 造成跨域
                .frameOptions()
                // 移除静态资源目录的安全控制，避免Spring Security默认禁止HTTP缓存的行为
                .and().cacheControl().disable();
        // 不创建 Session
        httpSecurity.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 认证请求处理
        httpSecurity
                // 全局共享规则
                .authorizeRequests()
                // 放开 actuator 端点
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                // 放开认证请求，包括token、logout...
                .antMatchers("/oauth/**", "/auth/**").permitAll()
                // 放开knife4j相关请求 https://gitee.com/xiaoym/knife4j/issues/I1Q5X6
                .antMatchers("/webjars/**", "/doc.html", "/swagger-resources/**", "/v2/api-docs", "/favicon.ico").permitAll()
                // 放开public、open-api、feign接口
                .mvcMatchers("/public/**", "/open-api/**", "/feign/**").permitAll()
                // 后面的所有请求都需要认证
                .anyRequest().authenticated();

        // 异常处理
        httpSecurity.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        // 添加 TokenAuthenticationFilter
        TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(environment, authTokenService(), securityProperties);
        httpSecurity.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authUserDetailsService)
                .passwordEncoder(passwordEncoder);

        auth.authenticationProvider(preAuthenticatedAuthenticationProvider());

        // TODO 属于后边可以扩展的Provider
//        auth.authenticationProvider(mobileAuthenticationProvider());
//        auth.authenticationProvider(emailAuthenticationProvider());
    }
}

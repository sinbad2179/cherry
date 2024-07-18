package cn.cherry.authenticate.config;

import cn.cherry.authenticate.handler.RestAccessDeniedHandler;
import cn.cherry.authenticate.handler.RestAuthenticationEntryPoint;
import cn.cherry.authenticate.config.properties.CherrySecurityProperties;
import cn.cherry.authenticate.service.AuthUserDetailsService;
import cn.cherry.authenticate.service.ConfigInMemoryClientDetailsService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.Assert;

import java.security.KeyPair;

/**
 * 自动装配
 *
 * @author :  sinbad.cheng
 * @since :  2024-07-18 14:11
 */
@Configuration
@EnableConfigurationProperties({CherrySecurityProperties.class})
public class CherrySecurityConfiguration {
    private final CherrySecurityProperties securityProperties;

    public CherrySecurityConfiguration(CherrySecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }


    /****************************自动装配start***************************/
    @Bean
    public AuthUserDetailsService authUserDetailsService() {
        return new AuthUserDetailsService();
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new AuthUserDetailsService();
    }

    /**
     * <p>客户端详情服务类</p>
     *
     * @see AuthorizationServerConfiguration
     * 这里设置 @Primary 避免 代理对象代理自身， 发生 StackoverflowError <p>
     * https://blog.csdn.net/qq_35425070/article/details/108915454 <p>
     */
    @Bean("cherryClientDetailsService")
    @Primary
    public ClientDetailsService clientDetailsService() {
        return new ConfigInMemoryClientDetailsService(securityProperties);
    }

    /**
     * 密码编码器，使用算法 bcrypt
     * https://github.com/xitu/gold-miner/blob/master/TODO1/password-hashing-pbkdf2-scrypt-bcrypt-and-argon2.md
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * 认证失败，Rest Json异常返回
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    /**
     * 没有访问权限，Rest Json异常返回
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }

    /**
     * 去除 ROLE_ 前缀
     */
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public KeyPair rsaKeyPair() {
        CherrySecurityProperties.RsaKey rsaKey = securityProperties.getRsaKey();
        Assert.notNull(rsaKey.getKeyStore(), "keyStore cannot be null");
        Assert.notNull(rsaKey.getKeyStorePassword(), "keyStorePassword cannot be null");
        Assert.notNull(rsaKey.getKeyAlias(), "keyAlias cannot be null");
        Assert.notNull(rsaKey.getKeyPassword(), "keyPassword cannot be null");

        ClassPathResource ksFile = new ClassPathResource(rsaKey.getKeyStore());
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(ksFile, rsaKey.getKeyStorePassword().toCharArray());
        return keyStoreKeyFactory.getKeyPair(rsaKey.getKeyAlias(), rsaKey.getKeyPassword().toCharArray());
    }






}

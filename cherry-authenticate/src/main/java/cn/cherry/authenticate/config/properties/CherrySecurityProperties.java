package cn.cherry.authenticate.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 安全配置
 *
 * @author :  sinbad.cheng
 * @since :  2024-07-18 13:44
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "cherry.security")
public class CherrySecurityProperties {

    /**
     * token 超时时间（有效期），默认8小时
     */
    private Duration tokenTimeout = Duration.ofSeconds(60L * 60 * 8);

    /**
     * refresh token 超时时间（有效期），默认7天
     */
    private Duration refreshTokenTimeout = Duration.ofSeconds(60L * 60 * 24 * 7);

    /**
     * 客户端详情
     */
    private List<BaseClientDetails> clients = new ArrayList<>();

    /**
     * 是否并发登录
     */
    private boolean isConcurrentLogin = true;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 万能测试验证码
     */
    private String testCode;

    /**
     * RSA key
     */
    private RsaKey rsaKey = new RsaKey();


    @Data
    public static class RsaKey {

        /**
         * The location of the key store.
         */
        private String keyStore;

        /**
         * The key store's password
         */
        private String keyStorePassword;

        /**
         * The alias of the key from the key store
         */
        private String keyAlias;

        /**
         * The password of the key from the key store
         */
        private String keyPassword;

    }

}

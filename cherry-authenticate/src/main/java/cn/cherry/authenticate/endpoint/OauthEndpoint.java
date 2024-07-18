package cn.cherry.authenticate.endpoint;

import cn.cherry.authenticate.util.CryptoUtil;
import cn.cherry.core.exception.BusinessException;
import cn.cherry.core.exception.enums.CommonErrorCode;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.Principal;
import java.util.Map;

/**
 * oauth2登录
 *
 * @author :  sinbad.cheng
 * @since :  2024-07-18 10:39
 */
@Slf4j
@RestController
@RequestMapping("/oauth")
public class OauthEndpoint {

    @Autowired
    private KeyPair rsaKeyPair;
    @Autowired
    private TokenEndpoint tokenEndpoint;


    /**
     * 获取 token 的请求
     * <p>
     * <p>
     * (name = "grant_type", defaultValue = "password", value = "授权模式", required = true),
     * (name = "client_id", value = "Oauth2客户端ID（新需放置在请求头）", required = true),
     * (name = "client_secret", value = "Oauth2客户端秘钥（需放置在请求头）", required = true),
     * (name = "username", defaultValue = "admin", value = "用户名"),
     * (name = "password", defaultValue = "123456", value = "密码"),
     * (name = "refresh_token", value = "刷新token"),
     * (name = "redirect_uri", value = "重定向 URI"),
     * (name = "state", value = "状态，客户端发起的随机值，用于防止 CSRF 攻击"),
     * (name = "scope", value = "请求的资源权限范围"),
     *
     * <p>
     *
     * @see TokenEndpoint#postAccessToken(Principal, Map)
     */
    @PostMapping("/token")
    public Object postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        /*TODO 比较简易的Oauth登录认证，主要打一个框架，后续根据实际业务再扩展！*/

        // 处理参数
        postProcessTokenParameters(parameters);

        // 是否使用标准返回
        return tokenEndpoint.postAccessToken(principal, parameters);
    }


    /**
     * 处理 token 参数
     */
    private void postProcessTokenParameters(Map<String, String> parameters) {
        // 手机号校验及格式化处理
        String mobile = parameters.get("mobile");
        if (StringUtils.isNotBlank(mobile)) {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber = null;
            try {
                phoneNumber = phoneNumberUtil.parse(mobile, "+86");
            } catch (NumberParseException e) {
                log.warn("输入手机号不正确：{}", mobile);
            }
            if (phoneNumber == null || !phoneNumberUtil.isValidNumber(phoneNumber)) {
                throw new BusinessException("输入手机号不正确");
            }
            mobile = "+" + phoneNumber.getCountryCode() + "-" + phoneNumber.getNationalNumber();
            parameters.put("mobile", mobile);
        }

        // 密码 rsa 解码
        try {
            String password = parameters.get("password");
            if (org.apache.commons.lang3.StringUtils.isNotBlank(password)) {
                Cipher decryptCipher = CryptoUtil.createDecryptCipher("RSA", rsaKeyPair.getPrivate());
                parameters.put("password", CryptoUtil.decryptToString(decryptCipher, password));
            }
        } catch (Throwable e) {
            log.warn("获取token认证，密码解密失败：{}", e.getMessage());
            throw new BusinessException(CommonErrorCode.SIGNATURE_VERIFICATION_FAILED);
        }
    }

}

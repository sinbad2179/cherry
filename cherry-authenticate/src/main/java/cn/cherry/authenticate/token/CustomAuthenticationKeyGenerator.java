package cn.cherry.authenticate.token;

import cn.cherry.authenticate.config.properties.CherrySecurityProperties;
import cn.cherry.authenticate.userdetails.AuthUserDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * 用于每次都生成新token，而不复用token
 *
 * @author :  sinbad.cheng
 * @since :  2024-07-18 16:33
 */
public class CustomAuthenticationKeyGenerator extends DefaultAuthenticationKeyGenerator {

    private final CherrySecurityProperties securityProperties;

    private static final String CLIENT_ID = "client_id";

    private static final String SCOPE = "scope";

    private static final String USERNAME = "username";

    public CustomAuthenticationKeyGenerator(CherrySecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }


    @Override
    public String extractKey(OAuth2Authentication authentication) {
        Map<String, String> values = new LinkedHashMap<String, String>();
        OAuth2Request authorizationRequest = authentication.getOAuth2Request();
        if (!authentication.isClientOnly()) {
            values.put(USERNAME, authentication.getName());
        }
        values.put(CLIENT_ID, authorizationRequest.getClientId());
        if (authorizationRequest.getScope() != null) {
            values.put(SCOPE, OAuth2Utils.formatParameterList(new TreeSet<String>(authorizationRequest.getScope())));
        }

        if (securityProperties.isConcurrentLogin()) {
            AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
            if (StringUtils.isNotBlank(userDetails.getAuthId())) {
                values.put("authId", userDetails.getAuthId());
            }
        }

        return generateKey(values);
    }
}

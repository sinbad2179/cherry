package cn.cherry.authenticate.token;

import cn.cherry.authenticate.userdetails.AuthUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author :  sinbad.cheng
 * @since :  2024-07-18 16:32
 */
public class AuthUserTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Authentication userAuthentication = authentication.getUserAuthentication();
        if (userAuthentication == null) {
            return accessToken;
        }

        Optional.ofNullable((AuthUserDetails) userAuthentication.getPrincipal()).ifPresent(userDetail -> {
            final Map<String, Object> additionInfo = new HashMap<>(32);
            additionInfo.put("userId", userDetail.getUserId());
            additionInfo.put("clientId", userDetail.getClientId());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionInfo);
        });

        return accessToken;
    }
}

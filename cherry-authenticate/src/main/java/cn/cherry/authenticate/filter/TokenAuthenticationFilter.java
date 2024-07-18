package cn.cherry.authenticate.filter;

import cn.cherry.authenticate.config.properties.CherrySecurityProperties;
import cn.cherry.authenticate.token.AuthTokenService;
import cn.cherry.core.exception.BaseErrorCode;
import cn.cherry.core.exception.enums.CommonErrorCode;
import cn.cherry.core.model.Result;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.MediaType;
import org.springframework.http.server.PathContainer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

/**
 * @author :  sinbad.cheng
 * @since :  2024-07-18 16:06
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    /***TODO 后边需要专门放在Constant类中维护****/
    String CURRENT_USER_NAME = "_CURRENT_USER";
    String AUTHORIZATION_HEADER = "Authorization";
    /**
     * 访问 Token
     */
    String ACCESS_TOKEN = "access_token";
    /**
     * token 类型前缀
     */
    String TOKEN_TYPE_PREFIX = "bearer ";


    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private final Environment environment;
    private final AuthTokenService authTokenService;
    private final CherrySecurityProperties securityProperties;
    private final PathPattern ADMIN_PATH_PATTERN;
    private final PathPattern API_PATH_PATTERN;
    private final PathPattern PUBLIC_PATH_PATTERN;
    private final boolean isDevOrTestProfile;

    public TokenAuthenticationFilter(Environment environment,
                                     AuthTokenService authTokenService,
                                     CherrySecurityProperties securityProperties) {
        this.environment = environment;
        this.authTokenService = authTokenService;
        this.securityProperties = securityProperties;
        // 这里自定义放开认证拦截
        ADMIN_PATH_PATTERN = new PathPatternParser().parse("/admin/**");
        API_PATH_PATTERN = new PathPatternParser().parse("/api/**");
        PUBLIC_PATH_PATTERN = new PathPatternParser().parse("/public/**");
        this.isDevOrTestProfile = environment.acceptsProfiles(Profiles.of("dev"));
        ;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        /*TODO @sinbad 判断 获取用户信息，存在则代表已认证过*/
        Object user = new Object();
        if (RandomUtil.randomInt(1, 10) > 5) {
            // TODO 还没开发 随机写死了
            processUserContext(request, user);

            chain.doFilter(request, response);
            return;
        }

        // 获取token值，为空直接返回
        String tokenValue = getAccessToken(request);

        // TODO 测试用户处理

        boolean isPublicRequest = PUBLIC_PATH_PATTERN.matches(PathContainer.parsePath(request.getServletPath()));


        OAuth2AccessToken accessToken = authTokenService.readAccessToken(tokenValue);
        if (accessToken == null || accessToken.isExpired()) {
            if (isPublicRequest) {
                chain.doFilter(request, response);
                return;
            }
            LOGGER.warn("accessToken为空或已过期：{}", tokenValue);
            writeErrorResponse(response, CommonErrorCode.LOGIN_EXPIRED);
            return;
        }

        user = buildUserByAccessToken(accessToken);


        // 设置用户信息、认证信息设置到SecurityContext、UserContext、RequestAttribute
        processUserContext(request, user);

        chain.doFilter(request, response);
    }

    private Object buildUserByAccessToken(OAuth2AccessToken accessToken) {
        Map<String, Object> additionalInformation = accessToken.getAdditionalInformation();
        Long userId = (Long) additionalInformation.get("userId");
        String clientId = (String) additionalInformation.get("clientId");
        Long countryVersionId = (Long) additionalInformation.get("countryVersionId");

        // TODO 查一下缓存

        return null;
    }


    /**
     * HTTP处理返回结果
     */
    public void writeErrorResponse(HttpServletResponse response, BaseErrorCode errorCode) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Result<Void> result = Result.failure(errorCode);
        result.setTraceId(MDC.get("traceId"));
        try (PrintWriter out = response.getWriter()) {
            out.append(JSON.toJSONString(result));
        }
    }


    /**
     * TODO 设置当前用户信息
     *
     * @param request HTTP请求
     * @param user    当前用户
     */
    public void processUserContext(HttpServletRequest request, Object user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user, null, Collections.emptyList());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 设置到用户上下文
//        UserContext.setUser(user);
        request.setAttribute(CURRENT_USER_NAME, user);
    }


    /**
     * 从请求中获取access_token
     */
    public String getAccessToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isBlank(token)) {
            return request.getParameter(ACCESS_TOKEN);
        }
        if (!StringUtils.startsWithIgnoreCase(token, TOKEN_TYPE_PREFIX)) {
            return null;
        }
        return token.substring(TOKEN_TYPE_PREFIX.length());
    }

}

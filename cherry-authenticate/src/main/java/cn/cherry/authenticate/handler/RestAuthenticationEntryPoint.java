package cn.cherry.authenticate.handler;

import cn.cherry.core.exception.enums.CommonErrorCode;
import cn.cherry.core.model.Result;
import cn.cherry.rpc.web.config.properties.WebRequestProperties;
import cn.cherry.rpc.web.util.HttpUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 认证失败 rest 返回
 *
 * @author :  sinbad.cheng
 * @since :  2024-07-18 14:18
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);
    @Autowired
    private WebRequestProperties requestProperties;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LOGGER.warn("请求未授权，url: {}，headers：{}，message: {}", request.getRequestURI(),
                JSON.toJSONString(buildHeaders(request)), authException.getMessage());

        Result<Void> result = Result.failure(CommonErrorCode.UNAUTHORIZED);
        result.setTraceId(MDC.get("traceId"));

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getOutputStream().write(JSON.toJSONBytes(result));
    }


    private Map<String, String> buildHeaders(HttpServletRequest request) {
        Set<String> logHeaders = requestProperties.getLogHeaders();
        if (CollectionUtils.isEmpty(logHeaders)) {
            return null;
        }

        Map<String, String> map = new HashMap<>();
        for (String header : logHeaders) {
            String headerValue = request.getHeader(header);
            if (headerValue != null) {
                map.put(header, headerValue);
            }
        }

        // 获取请求头信息，设置真实IP
        map.put("x-true-ip", HttpUtils.getRealIp(request));
        return map;
    }

}

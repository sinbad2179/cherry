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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 访问受限 rest 返回
 *
 * @author :  sinbad.cheng
 * @since :  2024-07-18 15:25
 */
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessDeniedHandler.class);
    @Autowired
    private WebRequestProperties requestProperties;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        LOGGER.warn("无访问权限，url: {}，headers：{}，message: {}", request.getRequestURI(),
                JSON.toJSONString(buildHeaders(request)), accessDeniedException.getMessage());

        Result<Void> result = Result.failure(CommonErrorCode.FORBIDDEN);
        result.setTraceId(MDC.get("traceId"));

        // write resp
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

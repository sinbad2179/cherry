package cn.cherry.rpc.web.interceptor;

import cn.cherry.core.common.constants.IConstants;
import cn.cherry.rpc.model.TraceLog;
import cn.cherry.rpc.web.config.properties.WebRequestProperties;
import cn.cherry.rpc.web.util.HttpUtils;
import com.alibaba.fastjson.JSON;
import okio.Okio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author: sinbad.cheng
 * @time: 2023/2/26 20:58
 * @description： 请求日志拦截器
 */
public class GateInterceptor implements HandlerInterceptor {

    private final static Logger TRACE_LOGGER = LoggerFactory.getLogger("trace");
    private static final Logger LOGGER = LoggerFactory.getLogger(GateInterceptor.class);

    /**
     * 应用名
     */
    private String SERVER_NAME;
    /**
     * 应用端口号
     */
    private String SERVER_PORT;

    /**
     * 请求配置
     */
    private WebRequestProperties webRequestProperties;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 设置当前请求时间
        request.setAttribute(IConstants.WebConstant.REQUEST_START_TIME, LocalDateTime.now());
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // void
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LocalDateTime requestTime = (LocalDateTime) request.getAttribute(IConstants.WebConstant.REQUEST_START_TIME);
        LocalDateTime endTime = LocalDateTime.now();
        long duration = Duration.between(requestTime, endTime).toMillis();

        TraceLog traceLog = new TraceLog();
        traceLog.setTraceId(MDC.get(IConstants.TRACE_ID))
                .setServerPort(SERVER_PORT)
                .setServerName(SERVER_NAME)
                .setMethod(request.getMethod())
                .setUri(request.getRequestURI())
                .setHeaders(buildHeaders(request))
                .setRequestParam(buildRequestParam(request))
                .setRequestBody(buildRequestBody(request))
                .setReqTime(requestTime)
                .setRespTime(endTime)
                .setDuration(duration)
                .setStatus(Objects.isNull(request.getAttribute(IConstants.WebConstant.EXCEPTION))
                        ?
                        "success" : "error")
                .setRespData(buildResponseData(request));
        // 打印日志
        String logJson = JSON.toJSONString(traceLog);
        TRACE_LOGGER.info(/*request.getRequestURI() +*/ "app_log_info:{}", logJson);

        if (duration >= webRequestProperties.getSlowTime()) {
            LOGGER.info("slow_request_log:{}", logJson);
        }

    }


    /**
     * 组装相应结果
     *
     * @param request
     * @return
     */
    private String buildResponseData(HttpServletRequest request) {
        Object attribute = request.getAttribute(IConstants.WebConstant.RESPONSE_DATA);
        return Objects.isNull(attribute) ? "" : JSON.toJSONString(attribute);
    }

    /**
     * 请求体
     *
     * @param request
     * @return
     */
    private String buildRequestBody(HttpServletRequest request) {
        String contentType = request.getContentType();
        MediaType mediaType = MediaType.parseMediaType(contentType);
        if (!MediaType.MULTIPART_FORM_DATA.isCompatibleWith(mediaType)) {
            try {
                // Source 对象转换为 BufferedSource 对象的方法，从而提高数据读取的效率
                return Okio.buffer(Okio.source(request.getInputStream())).readString(StandardCharsets.UTF_8);
            } catch (IOException e) {
                TRACE_LOGGER.error("err msg:{}", JSON.toJSON(e));
            }
        }
        return null;
    }

    /**
     * form请求信息
     *
     * @param request
     * @return
     */
    private String buildRequestParam(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String elementKey = parameterNames.nextElement();
            String parameter = request.getParameter(elementKey);
            stringBuilder
                    .append(elementKey)
                    .append("=")
                    .append(parameter)
                    .append("&");
        }
        if (stringBuilder.length() > 0) {
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }


    /**
     * 构建请求头
     *
     * @param request
     * @return
     */

    private Map<String, String> buildHeaders(HttpServletRequest request) {

        Set<String> logHeaders = webRequestProperties.getLogHeaders();
        if (CollectionUtils.isEmpty(logHeaders)) {
            return null;
        }
        Map<String, String> headersMap = new HashMap<>();
        for (String logHeader : logHeaders) {
            String headerVal = request.getHeader(logHeader);
            if (Objects.nonNull(headerVal)) {
                headersMap.put(logHeader, headerVal);
            }
        }
        headersMap.put("x-true-ip", HttpUtils.getRealIp(request));
        return headersMap;

    }
}

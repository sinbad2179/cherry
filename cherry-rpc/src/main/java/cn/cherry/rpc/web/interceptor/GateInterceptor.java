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
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
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
 * <p>
 * org.springframework.web.servlet.HandlerInterceptor@请求被处理之前或之后对请求进行拦截和处理
 */
public class GateInterceptor implements HandlerInterceptor {

    private final static Logger TRACE_LOGGER = LoggerFactory.getLogger("trace");
    private static final Logger LOGGER = LoggerFactory.getLogger(GateInterceptor.class);

    @Resource
    private Environment env;


    /**
     * 请求配置
     */
    private WebRequestProperties webRequestProperties;


    /**
     * 在请求被处理之前调用，可以进行一些前置处理，如权限验证等。如果返回 true，则继续执行下一个拦截器或处理器；如果返回 false，则中断请求处理流程。
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 设置当前请求时间
        request.setAttribute(IConstants.WebConstant.REQUEST_START_TIME, LocalDateTime.now());
        return true;
    }


    /**
     * 在请求被处理之后、视图被渲染之前调用，可以对请求的处理结果进行处理。但不能修改请求的内容和视图。
     *
     * @param request      current HTTP request
     * @param response     current HTTP response
     * @param handler      the handler (or {@link HandlerMethod}) that started asynchronous
     *                     execution, for type and/or instance examination
     * @param modelAndView the {@code ModelAndView} that the handler returned
     *                     (can also be {@code null})
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // void
    }


    /**
     * 请求处理完成后、视图也已经渲染完毕后调用，可以进行一些资源清理操作，如释放资源等。
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  the handler (or {@link HandlerMethod}) that started asynchronous
     *                 execution, for type and/or instance examination
     * @param ex       any exception thrown on handler execution, if any; this does not
     *                 include exceptions that have been handled through an exception resolver
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LocalDateTime requestTime = (LocalDateTime) request.getAttribute(IConstants.WebConstant.REQUEST_START_TIME);
        LocalDateTime endTime = LocalDateTime.now();
        long duration = Duration.between(requestTime, endTime).toMillis();

        TraceLog traceLog = new TraceLog();
        traceLog.setTraceId(MDC.get(IConstants.TRACE_ID))
                .setServerPort(env.getProperty("server.port"))
                .setServerName(env.getProperty("spring.application.name"))
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

        // 记录请求慢日志
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

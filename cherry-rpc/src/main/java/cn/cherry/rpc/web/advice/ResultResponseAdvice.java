package cn.cherry.rpc.web.advice;

import cn.cherry.core.common.constants.IConstants;
import cn.cherry.core.model.Result;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author: sinbad.cheng
 * @time: 2023/3/20 20:48
 * @description： 处理http请求响应结果
 */
@RestControllerAdvice
public class ResultResponseAdvice implements ResponseBodyAdvice<Object>, Ordered {

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // true 则回调beforeBodyWrite
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (!(body instanceof Result)) {
            return body;
        }
        Result<?> result = (Result<?>) body;
        // 处理链路ID
        processTraceId(result);
        // 处理参数格式化
        processArgsFormat(result);
        return result;
    }

    /**
     * 参数格式化处理
     *
     * @param result
     */
    private void processArgsFormat(Result<?> result) {
        String msg = result.getMsg();
        Object[] args = result.getArgs();
        if (ArrayUtils.isNotEmpty(args) && StringUtils.isNotBlank(msg)) {
            result.setMsg(String.format(msg, args));
            result.setArgs(null);
        }
    }

    /**
     * 设置traceId(链路ID)
     *
     * @param result
     */
    private void processTraceId(Result<?> result) {
        String traceId = result.getTraceId();
        if (StringUtils.isBlank(traceId)) {
            result.setTraceId(MDC.get(IConstants.TRACE_ID));
        }
    }

}

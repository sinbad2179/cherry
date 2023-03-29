package cn.cherry.rpc.web.config;

import cn.cherry.core.common.constants.IConstants;
import cn.cherry.core.exception.BusinessException;
import cn.cherry.core.exception.ServiceErrorCode;
import cn.cherry.core.model.Result;
import cn.cherry.rpc.web.config.properties.WebRequestProperties;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: sinbad.cheng
 * @time: 2023/3/21 20:16
 * @description：
 */
@ControllerAdvice
public class DefaultExceptionHandler implements Ordered {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Autowired
    private WebRequestProperties webRequestProperties;


    public Result<Void> handler(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
        request.setAttribute(IConstants.WebConstant.EXCEPTION, ex);

        String method = request.getMethod();
        String servletPath = request.getServletPath();

        boolean isBusinessEx = ex instanceof BusinessException;
        StringBuilder sb = new StringBuilder(64);
        sb.append(" ");
        sb.append(method);
        sb.append(servletPath);
        sb.append(" ");
        sb.append(isBusinessEx ? ex.getMessage() : Throwables.getStackTraceAsString(ex));

        if (isBusinessEx) {
            LOGGER.info(sb.toString());
        } else {
            LOGGER.error(sb.toString());
        }

        Result<Void> result = doException(ex);

        request.setAttribute(IConstants.WebConstant.RESPONSE_DATA, result);

        // 替换 response 状态码
        if (webRequestProperties.isReplaceResponseStatusCode()) {
            Integer resultCode = result.getCode();
            response.setStatus(resultCode >= 100 && resultCode < 600 ? resultCode : ServiceErrorCode.SERVER_ERROR.getCode());
        }

        return result;
    }


    public Result<Void> doException(Throwable ex) {
        return Result.failure(ServiceErrorCode.SERVER_ERROR);

    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
    
}

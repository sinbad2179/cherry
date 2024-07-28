package cn.cherry.rpc.web.config.properties;

import cn.cherry.core.common.constants.IConstants;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * @author: sinbad.cheng
 * @time: 2023/2/26 21:08
 * @description： 请求配置
 */
@ConfigurationProperties(prefix = WebRequestProperties.REQUEST_PREFIX)
public class WebRequestProperties {

    public static final String REQUEST_PREFIX = "cherry.request";

    /**
     * 慢请求时间
     */
    private Long slowTime = 2000L;

    /**
     * 是否替换 response 状态码
     */
    private boolean replaceResponseStatusCode = false;

    /**
     * log请求头
     */
    private Set<String> logHeaders = CollectionUtil.newLinkedHashSet(IConstants.WebConstant.APPLICATION_NAME_HEADER);

    public WebRequestProperties setSlowTime(Long slowTime) {
        this.slowTime = slowTime;
        return this;
    }

    public WebRequestProperties setReplaceResponseStatusCode(boolean replaceResponseStatusCode) {
        this.replaceResponseStatusCode = replaceResponseStatusCode;
        return this;
    }

    public WebRequestProperties setLogHeaders(Set<String> logHeaders) {
        this.logHeaders = logHeaders;
        return this;
    }

    public Long getSlowTime() {
        return slowTime;
    }

    public boolean isReplaceResponseStatusCode() {
        return replaceResponseStatusCode;
    }

    public Set<String> getLogHeaders() {
        return logHeaders;
    }

    @Override
    public String toString() {
        return "WebRequestProperties{" +
                "slowTime=" + slowTime +
                ", replaceResponseStatusCode=" + replaceResponseStatusCode +
                ", logHeaders=" + logHeaders +
                '}';
    }
}

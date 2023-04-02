package cn.cherry.rpc.web.config.properties;

import cn.cherry.core.common.constants.IConstants;
import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * @author: sinbad.cheng
 * @time: 2023/2/26 21:08
 * @description： 请求配置
 */
@Data
@ToString
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


}

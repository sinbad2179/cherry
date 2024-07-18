package cn.cherry.authenticate.service;

import cn.cherry.authenticate.config.properties.CherrySecurityProperties;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 配置化客户端详情服务
 *
 * @author :  sinbad.cheng
 * @see org.springframework.security.oauth2.provider.ClientDetailsService
 * @since :  2024-07-18 13:43
 */
public class ConfigInMemoryClientDetailsService extends InMemoryClientDetailsService {

    private final CherrySecurityProperties securityProperties;


    public ConfigInMemoryClientDetailsService(CherrySecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @PostConstruct
    public void initClients() {
        List<BaseClientDetails> clients = securityProperties.getClients();
        if (!CollectionUtils.isEmpty(clients)) {
            Map<String, BaseClientDetails> clientDetailsMap = clients.stream()
                    .collect(Collectors.toMap(BaseClientDetails::getClientId, Function.identity()));
            setClientDetailsStore(clientDetailsMap);
        }
    }

}

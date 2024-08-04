package cn.cherry.synces.config;

import cn.cherry.synces.model.EsMetaModel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties("es")
@Data
public class EsYamlMetaConfig {

    private Map<String, EsMetaModel> indexMetaMap = new HashMap<>();

}

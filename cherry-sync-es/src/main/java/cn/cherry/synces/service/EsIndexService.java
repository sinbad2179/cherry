package cn.cherry.synces.service;

import cn.cherry.synces.config.EsYamlMetaConfig;
import cn.cherry.synces.model.EsMetaModel;
import cn.cherry.synces.model.EsRequestModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.elasticsearch.action.DocWriteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * es索引处理
 */
@Slf4j
@Component
public class EsIndexService {
    /**
     * 日志开关
     */
    @Value("${application.log:10}")
    private Integer randomLog;
    private final static String TABLE = "sys_user";


    @Autowired
    private EsBulkService esBulkService;
    @Autowired
    private EsYamlMetaConfig esYamlMetaConfig;


    /**
     * Kafka消息处理
     *
     * @param records
     */
    public void index(List<ConsumerRecord<?, ?>> records) {
        List<EsRequestModel> esRequestModelList = records.parallelStream().map(this::buildDocWriteRequest).filter(item -> !Objects.isNull(item)).collect(Collectors.toList());
        // 确保消息顺序
        esRequestModelList.sort(Comparator.comparing(EsRequestModel::getVersion));
        List<DocWriteRequest> docWriteRequests = esRequestModelList.stream().map(EsRequestModel::buildDocWriteRequest).collect(Collectors.toList());
        esBulkService.bulk(docWriteRequests);
    }

    private EsRequestModel buildDocWriteRequest(ConsumerRecord<?, ?> record) {
        long offset = record.offset();
        String value = (String) record.value();

        //日志开关
        if (RandomUtils.nextInt(1, 100) < randomLog) {
            log.info("oms_message: {}", value);
        }

        EsMetaModel esMetaModel = esYamlMetaConfig.getIndexMetaMap().get(TABLE);


    }
}

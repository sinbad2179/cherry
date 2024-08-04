package cn.cherry.synces.consumer;


import cn.cherry.synces.service.EsIndexService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DemonConsumer {

    @Autowired
    private EsIndexService esIndexService;

    @KafkaListener(topics = {"ogg-waybill-es"}, groupId = "ogg-waybill-es_yl-nwm-ogg-es_es-data", errorHandler = "listenerErrorHandler")
    public void onMessage(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        try {
            esIndexService.index(records);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("message_consumer error, {}", JSON.toJSONString(records), e);
            throw new ListenerExecutionFailedException(e.getMessage());
        }
    }

}

package cn.cherry.synces.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Configuration
public class SyncEsListenerErrorHandler {

    @Bean
    public ConsumerAwareListenerErrorHandler listenerErrorHandler() {
        return new ConsumerAwareListenerErrorHandler() {
            @Override
            public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
                List<ConsumerRecord> payload = (List<ConsumerRecord>) message.getPayload();
                if (!CollectionUtils.isEmpty(payload)) {
                    ConsumerRecord record = payload.get(0);
                    String topic = record.topic();
                    int partition = record.partition();
                    long offset = record.offset();
                    consumer.seek(new TopicPartition(topic, partition), offset);
                }
                try {
                    //降低异常重试频率，避免空转
                    Thread.sleep(10000);
                } catch (Exception e) {
                    //ignore
                }
                return null;
            }
        };
    }
}

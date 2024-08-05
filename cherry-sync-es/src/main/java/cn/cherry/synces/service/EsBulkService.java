package cn.cherry.synces.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class EsBulkService {
    @Autowired
    private RestHighLevelClient client;

    /**
     * @see RestHighLevelClient#bulk(BulkRequest, RequestOptions)
     */
    public void bulk(List<DocWriteRequest> docWriteRequests) {
        if (CollectionUtils.isEmpty(docWriteRequests)) {
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        BulkResponse bulkResponse;
        try {
            bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("bulk error", e);
            throw new RuntimeException("bulk error，" + e.getMessage(), null);
        }
        // 失败处理
        if (bulkResponse.hasFailures()) {
            BulkItemResponse[] items = bulkResponse.getItems();
            if (ArrayUtils.isEmpty(items)) {
                log.error("index bulk error: {}", JSON.toJSONString(bulkResponse));
                throw new RuntimeException("es bulk error");
            }

            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if (bulkItemResponse.isFailed()) {
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                    if (RestStatus.CONFLICT == failure.getStatus()) {
                        if (failure.toString().contains("is higher than the one provided")) {
                            continue;
                        }
                        log.warn("Version Conflicted. {}", failure);
                        throw new RuntimeException("ES error:Version Conflicted!" + failure.toString());
                    }

                    if (RestStatus.NOT_FOUND == failure.getStatus()) {
                        log.warn("update record not found, ignored.{}", failure.getMessage());
                        continue;
                    }
                    log.error("bulk fail one:{}", failure);
                    throw new RuntimeException(failure.toString());
                }
            }
        }
    }

}

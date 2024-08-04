package cn.cherry.synces.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class EsBulkService {
    @Autowired
    private RestHighLevelClient client;

    /**
     * @param docWriteRequests
     */
    public void bulk(List<DocWriteRequest> docWriteRequests) {

//        client.bulk();
    }

}

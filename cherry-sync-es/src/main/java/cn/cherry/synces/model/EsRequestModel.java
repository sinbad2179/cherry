package cn.cherry.synces.model;

import cn.cherry.synces.enums.OggOpTypeEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.xcontent.XContentType;

import java.util.Date;
import java.util.Map;

@Data
public class EsRequestModel {
    private OggOpTypeEnum opTypeEnum;
    private String index;
    private String type;
    private String id;
    private String routingKey;
    private Date createTime;
    private Map data;
    private long version;


    public DocWriteRequest<?> buildDocWriteRequest() {
        DocWriteRequest<?> docWriteRequest = null;
        if (OggOpTypeEnum.INSERT.equals(opTypeEnum)) {
            docWriteRequest = buildDocWriteRequestByInsert();
        } else if (OggOpTypeEnum.UPDATE.equals(opTypeEnum)) {
            docWriteRequest = buildDocWriteRequestByUpdate();
        } else if (OggOpTypeEnum.DELETE.equals(opTypeEnum)) {
            docWriteRequest = buildDocWriteRequestByDelete();
        }
        if (docWriteRequest == null) {
            return null;
        }
        if (StringUtils.isNotEmpty(routingKey)) {
            docWriteRequest.routing(routingKey);
        }
        return docWriteRequest;
    }

    private DocWriteRequest<?> buildDocWriteRequestByInsert() {
        return new IndexRequest(index, type, id).source(data, XContentType.JSON);
    }

    private DocWriteRequest<?> buildDocWriteRequestByUpdate() {
        return new UpdateRequest(index, type, id).doc(data, XContentType.JSON);
    }

    private DocWriteRequest<?> buildDocWriteRequestByDelete() {
        return new DeleteRequest(index, type, id);
    }


}

package cn.cherry.synces.utils;

import cn.cherry.synces.enums.OggOpTypeEnum;
import cn.cherry.synces.model.EsMetaModel;
import cn.cherry.synces.model.EsRequestModel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author :  sinbad.cheng
 * @since :  2024-08-05 18:07
 */
@Slf4j
public class Ogg2EsMessageUtil {

    public static EsRequestModel toEsMessage(String value, EsMetaModel esMetaModel) {
        JSONObject recordJSON = JSON.parseObject(value);

        String opType = recordJSON.getString("op_type");
        JSONObject beforeObj = recordJSON.getJSONObject("before");
        JSONObject afterObj = recordJSON.getJSONObject("after");
        OggOpTypeEnum opTypeEnum = OggOpTypeEnum.value(opType);
        if (opTypeEnum != OggOpTypeEnum.INSERT && opTypeEnum != OggOpTypeEnum.UPDATE && opTypeEnum != OggOpTypeEnum.DELETE) {
            log.info("操作类型不需要处理:{}", opTypeEnum);
            return null;
        }

        JSONObject oggJSON = null;
        switch (opTypeEnum) {
            case INSERT:
            case UPDATE:
                oggJSON = afterObj;
                break;
            case DELETE:
                oggJSON = beforeObj;
                break;
        }

        JSONObject message = esData(esMetaModel, oggJSON);
        EsRequestModel requestModel = new EsRequestModel();
        requestModel.setOpTypeEnum(opTypeEnum);
        requestModel.setIndex(esMetaModel.getIndex());
        requestModel.setType(esMetaModel.getType());
        requestModel.setId(message.getString(esMetaModel.getPrimaryColumn()));
        requestModel.setData(message);

        String createTimeColumn = esMetaModel.getCreateTimeColumn();
        if (StringUtils.isNotBlank(createTimeColumn)) {
            requestModel.setCreateTime(message.getDate(esMetaModel.getCreateTimeColumn()));
        }
        return requestModel;
    }

    private static JSONObject esData(EsMetaModel esMetaModel, JSONObject data) {
        List<String> whiteListColumns = esMetaModel.getColumns();
        JSONObject result = new JSONObject();
        if (!CollectionUtils.isEmpty(data)) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (CollectionUtils.isEmpty(whiteListColumns) || whiteListColumns.contains(key)) {
                    result.put(key, value);
                }
            }
        }
        return result;
    }
}

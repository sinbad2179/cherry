package cn.cherry.synces.service;

import cn.cherry.synces.config.EsYamlMetaConfig;
import cn.cherry.synces.mapper.SysUserMapper;
import cn.cherry.synces.model.EsMetaModel;
import cn.cherry.synces.model.EsRequestModel;
import cn.cherry.synces.model.entity.SysUser;
import cn.cherry.synces.utils.Ogg2EsMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.elasticsearch.action.DocWriteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
    @Autowired
    private SysUserMapper sysUserMapper;


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
        long version = record.offset();
        String value = (String) record.value();

        //日志开关
        if (RandomUtils.nextInt(1, 100) < randomLog) {
            log.info("oms_message: {}", value);
        }

        EsMetaModel esMetaModel = esYamlMetaConfig.getIndexMetaMap().get(TABLE);
        EsRequestModel requestModel = Ogg2EsMessageUtil.toEsMessage(value, esMetaModel);
        if (requestModel == null) {
            return null;
        }
        String primaryId = requestModel.getId();
        Date createTime = requestModel.getCreateTime();
        if (null == createTime && StringUtils.isNoneBlank(primaryId)) {
            SysUser sysUser = sysUserMapper.selectById(primaryId);
            if (Objects.nonNull(sysUser) && Objects.nonNull(sysUser.getCreateTime())) {
                ZoneId zoneId = ZoneId.systemDefault();
                ZonedDateTime zdt = sysUser.getCreateTime().atZone(zoneId);
                createTime = Date.from(zdt.toInstant());
            }
        }
        if (createTime == null) {
            log.error("index_error, createTime is null, value={}", value);
            return null;
        }

        requestModel.setVersion(version);
        requestModel.setIndex(buildIndex(requestModel.getIndex(), createTime));

        return requestModel;
    }

    private String buildIndex(String index, Date createTime) {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return String.format("%s_%s", index, simpleDateFormat.format(createTime));
    }
}

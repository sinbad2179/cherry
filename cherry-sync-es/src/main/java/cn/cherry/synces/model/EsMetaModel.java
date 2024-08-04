package cn.cherry.synces.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsMetaModel {
    private String index;
    private String type;
    private String primaryColumn;
    private String writeAliasSuffix = "";
    private List<String> columns = new ArrayList<>();

    private String createTimeColumn;
    private String createTimeFormat = "yyyy-MM-dd HH:mm:ss";

    private String indexSuffixFormat;
    private String routingColumn;
    private String versionField;
}

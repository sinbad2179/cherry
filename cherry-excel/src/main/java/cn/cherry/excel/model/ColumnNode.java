package cn.cherry.excel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: sinbad.cheng
 * @time: 2023/4/2 18:28
 * @description： 自定义列
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnNode implements Serializable {
    private static final long serialVersionUID = -6732126202614372591L;

    /**
     * 列名称
     */
    private String columnName;

    /**
     * 列
     */
    private String column;

    private Boolean show;

    private Integer position;
}

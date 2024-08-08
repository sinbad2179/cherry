package cn.cherry.excel.util;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author :  sinbad.cheng
 * @since :  2024-07-23 13:47
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SysCountryExcelVO implements Serializable {

    @ExcelProperty("国家编码")
    private String countryCode;

    @ExcelProperty("国家名称（原文）")
    private String countryName;

    /**
     * 手机国家区号
     */
    @ExcelProperty("手机国家区号")
    private String mobileCountryCode;

    /**
     * 手机号长度下限
     */
    @ExcelProperty("手机号长度下限")
    private String mobileLengthDown;

    /**
     * 手机号长度上限
     */
    @ExcelProperty("手机号长度上限")
    private String mobileLengthUp;

    @ExcelProperty("排序")
    private String sort;

}

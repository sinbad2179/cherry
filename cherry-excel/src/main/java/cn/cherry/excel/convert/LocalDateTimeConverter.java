package cn.cherry.excel.convert;

import cn.cherry.core.util.LocalDateUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.DateUtils;

import java.time.LocalDateTime;

/**
 * LocalDateTime 时间转换器
 *
 * @author :  sinbad.cheng
 * @since :  2024-08-08 15:42
 */
public class LocalDateTimeConverter implements Converter<LocalDateTime> {
    public static final LocalDateTimeConverter INSTANCE = new LocalDateTimeConverter();

    private static final String MINUS = "-";


    @Override
    public Class<LocalDateTime> supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDateTime convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
                                           GlobalConfiguration globalConfiguration) {
        String stringValue = cellData.getStringValue();

        String pattern;
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            pattern = switchDateFormat(stringValue);
        } else {
            pattern = contentProperty.getDateTimeFormatProperty().getFormat();
        }

        return LocalDateUtil.parseDateTime(stringValue, pattern);
    }

    @Override
    public WriteCellData<String> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        String pattern;
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            pattern = DateUtils.DATE_FORMAT_19;
        } else {
            pattern = contentProperty.getDateTimeFormatProperty().getFormat();
        }

        return new WriteCellData<>(LocalDateUtil.format(value, pattern));
    }

    /**
     * switch date format
     *
     * @param dateString dateString
     * @return pattern
     */
    private static String switchDateFormat(String dateString) {
        int length = dateString.length();
        switch (length) {
            case 19:
                if (dateString.contains(MINUS)) {
                    return DateUtils.DATE_FORMAT_19;
                } else {
                    return DateUtils.DATE_FORMAT_19_FORWARD_SLASH;
                }
            case 17:
                return DateUtils.DATE_FORMAT_17;
            case 14:
                return DateUtils.DATE_FORMAT_14;
            case 10:
                return DateUtils.DATE_FORMAT_10;
            default:
                throw new IllegalArgumentException("can not find date format for: " + dateString);
        }
    }

}

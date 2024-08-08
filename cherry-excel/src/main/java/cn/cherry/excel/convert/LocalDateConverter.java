package cn.cherry.excel.convert;

import cn.cherry.core.util.LocalDateUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.text.ParseException;
import java.time.LocalDate;

/**
 * LocalDate 时间转换器
 *
 * @author :  sinbad.cheng
 * @since :  2024-08-08 15:42
 */
public class LocalDateConverter implements Converter<LocalDate> {
    public static final LocalDateConverter INSTANCE = new LocalDateConverter();

    @Override
    public Class<LocalDate> supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDate convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws ParseException {
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return LocalDateUtil.parseDate(cellData.getStringValue());
        }

        String format = contentProperty.getDateTimeFormatProperty().getFormat();
        return LocalDateUtil.parseDate(cellData.getStringValue(), format);
    }

    @Override
    public WriteCellData<String> convertToExcelData(LocalDate value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return new WriteCellData<>(LocalDateUtil.format(value));
        }

        String format = contentProperty.getDateTimeFormatProperty().getFormat();
        return new WriteCellData<>(LocalDateUtil.format(value, format));
    }

}

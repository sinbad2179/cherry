package cn.cherry.excel.util;

import cn.cherry.excel.convert.LocalDateConverter;
import cn.cherry.excel.convert.LocalDateTimeConverter;
import cn.cherry.excel.listener.ExcelReadListener;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Excel 工具类
 *
 * @author sinbad.cheng
 * @date 2024/7/28
 */
@Slf4j
public class ExcelUtils {

    private static final Map<Class<?>, Map<Integer, Head>> DYNAMIC_HEAD_CACHE = new ConcurrentHashMap<>(256);

    /**
     * 读取Excel所有sheet数据
     *
     * @param inputStream Excel文件流
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> read(InputStream inputStream, Class<T> clazz) {
        ExcelReadListener<T> excelReadListener = new ExcelReadListener<>();

        ExcelReaderBuilder builder = getReaderBuilder(inputStream, excelReadListener, clazz);
        builder.doReadAll();

        return excelReadListener.getDataList();
    }

    public static <T> List<T> read(InputStream inputStream, Class<T> clazz, Function<String, String> i18nFunction) {
        ExcelReadListener<T> excelReadListener = new ExcelReadListener<>();
        ExcelReaderBuilder builder = getReaderBuilder(inputStream, excelReadListener, clazz, i18nFunction);
        builder.doReadAll();
        return excelReadListener.getDataList();
    }


    private static <T> ExcelReaderBuilder getReaderBuilder(InputStream inputStream, ExcelReadListener<T> excelReadListener, Class<T> clazz) {

        try {
            inputStream = new BufferedInputStream(inputStream);
            return EasyExcel.read(inputStream).head(clazz).useDefaultListener(false)
                    // TODO 需要时拓展
//                    .registerReadListener(new RockerModelBuildEventListener())
//                    .registerReadListener(new DynamicHeadReadListener<>(readListener))
                    .registerConverter(LocalDateConverter.INSTANCE)
                    .registerConverter(LocalDateTimeConverter.INSTANCE)
                    ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取构建类
     *
     * @param inputStream  excel文件流
     * @param readListener excel监听类sdf
     * @return ExcelReaderBuilder
     */
    public static <T> ExcelReaderBuilder getReaderBuilder(InputStream inputStream, AnalysisEventListener<T> readListener,
                                                          Class<T> clazz, Function<String, String> i18nFunction) {
        try {
            inputStream = new BufferedInputStream(inputStream);
            return EasyExcel.read(inputStream).head(clazz).useDefaultListener(false)
//                    .registerReadListener(new RockerModelBuildEventListener())
//                    .registerReadListener(new I18nHeaderReadListener(i18nFunction))
//                    .registerReadListener(new DynamicHeadReadListener<>(readListener))
//                    .registerConverter(LocalDateConverter.INSTANCE)
//                    .registerConverter(LocalDateTimeConverter.INSTANCE)
                    ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

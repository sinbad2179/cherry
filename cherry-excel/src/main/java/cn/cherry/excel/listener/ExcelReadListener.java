package cn.cherry.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel 读取监听处理
 *
 * @author sinbad.cheng
 * @date 2024/7/28
 */
public class ExcelReadListener<T> extends AnalysisEventListener<T> {

    /**
     * 数据缓存
     */
    private final List<T> dataList = new ArrayList<>(512);

    @Override
    public void invoke(T data, AnalysisContext context) {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    public List<T> getDataList() {
        return dataList;
    }
}

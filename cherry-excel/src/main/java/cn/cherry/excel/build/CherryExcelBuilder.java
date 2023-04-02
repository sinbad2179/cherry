package cn.cherry.excel.build;

import com.alibaba.excel.context.WriteContext;
import com.alibaba.excel.write.ExcelBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.fill.FillConfig;

import java.util.Collection;

/**
 * @author: sinbad.cheng
 * @time: 2023/2/22 22:33
 * @description：
 */
public class CherryExcelBuilder implements ExcelBuilder {
    @Override
    public void addContent(Collection<?> collection, WriteSheet writeSheet) {

    }

    @Override
    public void addContent(Collection<?> collection, WriteSheet writeSheet, WriteTable writeTable) {

    }

    @Override
    public void fill(Object o, FillConfig fillConfig, WriteSheet writeSheet) {

    }

    @Override
    public void merge(int i, int i1, int i2, int i3) {

    }

    @Override
    public WriteContext writeContext() {
        return null;
    }

    @Override
    public void finish(boolean b) {

    }
}

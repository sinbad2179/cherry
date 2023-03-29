package cn.cherry.core.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Iterables;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 批量调用工具类
 *
 * @author <a href="kafka@88.com">刘伟(Marvin)</a>
 * @since 2022-09-01
 */
public class PartInvokeUtil {

    /**
     * 分批执行获取
     *
     * @param invoke    调用方法
     * @param params    参数
     * @param batchSize 批次大小
     * @return 执行结果
     */
    public static <P, V> List<V> partToList(Function<Collection<P>, List<V>> invoke, Collection<P> params, int batchSize) {
        if (CollectionUtil.isEmpty(params)) {
            return Collections.emptyList();
        }

        List<V> list = new ArrayList<>(params.size());
        Iterable<List<P>> partition = Iterables.partition(params, batchSize);
        for (List<P> ps : partition) {
            List<V> results = invoke.apply(ps);
            if (CollectionUtil.isNotEmpty(results)) {
                list.addAll(results);
            }
        }

        return list;
    }

    /**
     * 分批执行获取
     *
     * @param invoke    调用方法
     * @param params    参数
     * @param batchSize 批次大小
     * @return 执行结果
     */
    public static <K, P, V> Map<K, V> partToMap(Function<Collection<P>, Map<K, V>> invoke, Collection<P> params, int batchSize) {
        if (CollectionUtil.isEmpty(params)) {
            return Collections.emptyMap();
        }

        HashMap<K, V> hashMap = new HashMap<>(Math.max((int) (params.size() / .75f) + 1, 16));
        Iterable<List<P>> partition = Iterables.partition(params, batchSize);

        for (List<P> ps : partition) {
            Map<K, V> results = invoke.apply(ps);
            if (MapUtil.isNotEmpty(results)) {
                hashMap.putAll(results);
            }
        }

        return hashMap;
    }

    /**
     * 分批次执行
     *
     * @param invoke    调用方法
     * @param records   消费记录
     * @param batchSize 批次大小
     */
    public static <V> void partInvoke(Consumer<Collection<V>> invoke, Collection<V> records, int batchSize) {
        if (CollectionUtil.isEmpty(records)) {
            return;
        }

        Iterable<List<V>> partition = Iterables.partition(records, batchSize);
        for (List<V> pr : partition) {
            invoke.accept(pr);
        }
    }


}
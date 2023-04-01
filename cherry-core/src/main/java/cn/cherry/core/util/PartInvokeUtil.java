package cn.cherry.core.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 批量调用工具类
 *
 * @author sinbad
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

    /**
     * 分批获取结果集
     *
     * @param invoke 执行函数
     * @param ids    id集合
     * @param limit  一批最大查询的次数
     * @param <P>    id
     * @param <V>    结果
     * @return 结果集
     */
    public static <P, V> List<V> partGet(Function<List<P>, List<V>> invoke, List<P> ids, int limit) {
        if (CollectionUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        int size = ids.size();
        List<V> list = new ArrayList<>();
        List<P> subIds;
        if (size > limit) {
            int count = size / limit;
            if (size % limit != 0) {
                count += 1;
            }
            int length = 0;
            for (int i = 0; i < count; i++) {
                if (i == count - 1) {
                    limit = size - (i * limit);
                }
                subIds = ids.subList(length, length + limit);
                length += limit;
                if (CollectionUtil.isEmpty(subIds)) {
                    break;
                }
                List subTemp = invoke.apply(subIds);
                if (subTemp != null) {
                    list.addAll(subTemp);
                }
            }
        } else {
            List tmp = invoke.apply(ids);
            if (tmp != null) {
                list.addAll(tmp);
            }
        }
        return list;
    }

    /**
     * 分批获取结果集
     *
     * @param invoke 执行函数
     * @param ids    id数组
     * @param limit  一批最大查询的次数
     * @param <P>    id
     * @param <V>    结果
     * @return 结果集
     */
    public static <P, V> List<V> partGet(Function<P[], List<V>> invoke, P[] ids, int limit) {
        if (ArrayUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        int size = ids.length;
        List<V> list = new ArrayList<>();
        P[] subIds;
        if (size > limit) {
            int count = size / limit;
            if (size % limit != 0) {
                count += 1;
            }
            int length = 0;
            for (int i = 0; i < count; i++) {
                if (i == count - 1) {
                    limit = size - (i * limit);
                }
                subIds = Arrays.copyOfRange(ids, length, length + limit);
                length += limit;
                if (ArrayUtils.isEmpty(subIds)) {
                    break;
                }
                list.addAll(invoke.apply(subIds));
            }
        } else {
            list.addAll(invoke.apply(ids));
        }
        return list;
    }

    /**
     * 分批执行
     *
     * @param invoke 执行函数
     * @param rows   结果集集合
     * @param limit  一批最大执行的次数
     * @param <T>    结果
     */
    public static <T> void partInvoke(Consumer<List<T>> invoke, List<T> rows, int limit) {
        if (CollectionUtil


                .isEmpty(rows)) {
            return;
        }
        int size = rows.size();
        List<T> subRows;
        if (size > limit) {
            int count = size / limit;
            if (size % limit != 0) {
                count += 1;
            }
            int length = 0;
            for (int i = 0; i < count; i++) {
                if (i == count - 1) {
                    limit = size - (i * limit);
                }
                subRows = rows.subList(length, length + limit);
                length += limit;
                if (CollectionUtil


                        .isEmpty(subRows)) {
                    break;
                }
                invoke.accept(subRows);
            }
        } else {
            invoke.accept(rows);
        }
    }

    /**
     * 分批执行
     *
     * @param invoke 执行函数
     * @param rows   结果集数组
     * @param limit  一批最大执行的次数
     * @param <T>    结果
     */
    public static <T> void partInvoke(Consumer<T[]> invoke, T[] rows, int limit) {
        if (ArrayUtils.isEmpty(rows)) {
            return;
        }
        int size = rows.length;
        T[] subRows;
        if (size > limit) {
            int count = size / limit;
            if (size % limit != 0) {
                count += 1;
            }
            int length = 0;
            for (int i = 0; i < count; i++) {
                if (i == count - 1) {
                    limit = size - (i * limit);
                }
                subRows = Arrays.copyOfRange(rows, length, length + limit);
                length += limit;
                if (ArrayUtils.isEmpty(subRows)) {
                    break;
                }
                invoke.accept(subRows);
            }
        } else {
            invoke.accept(rows);
        }
    }

    /**
     * 分批执行
     *
     * @param invoke 执行函数
     * @param rows   结果集集合
     * @param limit  一批最大执行的次数
     * @param <T>    结果
     * @return 影响行数
     */
    public static <T> Integer partInvokeIntResult(Function<List<T>, Integer> invoke, List<T> rows, int limit) {
        if (CollectionUtil.isEmpty(rows)) {
            return 0;
        }
        int size = rows.size();
        Integer result = 0;
        List<T> subRows;
        if (size > limit) {
            int count = size / limit;
            if (size % limit != 0) {
                count += 1;
            }
            int length = 0;
            for (int i = 0; i < count; i++) {
                if (i == count - 1) {
                    limit = size - (i * limit);
                }
                subRows = rows.subList(length, length + limit);
                length += limit;
                if (CollectionUtil


                        .isEmpty(subRows)) {
                    break;
                }
                result += invoke.apply(subRows);
            }
        } else {
            result += invoke.apply(rows);
        }
        return result;
    }

    /**
     * 分批执行
     *
     * @param invoke 执行函数
     * @param rows   结果集数组
     * @param limit  一批最大执行的次数
     * @param <T>    结果
     * @return 影响行数
     */
    public static <T> Integer partInvokeIntResult(Function<T[], Integer> invoke, T[] rows, int limit) {
        if (ArrayUtils.isEmpty(rows)) {
            return 0;
        }
        int size = rows.length;
        Integer result = 0;
        T[] subRows;
        if (size > limit) {
            int count = size / limit;
            if (size % limit != 0) {
                count += 1;
            }
            int length = 0;
            for (int i = 0; i < count; i++) {
                if (i == count - 1) {
                    limit = size - (i * limit);
                }
                subRows = Arrays.copyOfRange(rows, length, length + limit);
                length += limit;
                if (ArrayUtils.isEmpty(subRows)) {
                    break;
                }
                result += invoke.apply(subRows);
            }
        } else {
            result += invoke.apply(rows);
        }
        return result;
    }

    /**
     * 分批执行
     *
     * @param invoke 执行函数
     * @param rows   结果集集合
     * @param limit  一批最大执行的次数
     * @param <T>    结果
     * @return 影响行数
     */
    public static <T> Long partInvokeLongResult(Function<List<T>, Long> invoke, List<T> rows, int limit) {
        if (CollectionUtil


                .isEmpty(rows)) {
            return 0L;
        }
        int size = rows.size();
        Long result = 0L;
        List<T> subRows;
        if (size > limit) {
            int count = size / limit;
            if (size % limit != 0) {
                count += 1;
            }
            int length = 0;
            for (int i = 0; i < count; i++) {
                if (i == count - 1) {
                    limit = size - (i * limit);
                }
                subRows = rows.subList(length, length + limit);
                length += limit;
                if (CollectionUtil


                        .isEmpty(subRows)) {
                    break;
                }
                result += invoke.apply(subRows);
            }
        } else {
            result += invoke.apply(rows);
        }
        return result;
    }

    /**
     * 分批执行
     *
     * @param invoke 执行函数
     * @param rows   结果集数组
     * @param limit  一批最大执行的次数
     * @param <T>    结果
     * @return 影响行数
     */
    public static <T> Long partInvokeLongResult(Function<T[], Long> invoke, T[] rows, int limit) {
        if (ArrayUtils.isEmpty(rows)) {
            return 0L;
        }
        int size = rows.length;
        Long result = 0L;
        T[] subRows;
        if (size > limit) {
            int count = size / limit;
            if (size % limit != 0) {
                count += 1;
            }
            int length = 0;
            for (int i = 0; i < count; i++) {
                if (i == count - 1) {
                    limit = size - (i * limit);
                }
                subRows = Arrays.copyOfRange(rows, length, length + limit);
                length += limit;
                if (ArrayUtils.isEmpty(subRows)) {
                    break;
                }
                result += invoke.apply(subRows);
            }
        } else {
            result += invoke.apply(rows);
        }
        return result;
    }


    /**
     * 分批执行
     *
     * @param invoke      执行函数
     * @param invokeModel 执行参数，结果集和Id集合
     * @param limit       一批最大执行的次数
     * @param <P>         id
     * @param <V>         结果
     */
    public static <P, V> void partInvoke(Consumer<InvokeModel<P, V>> invoke, InvokeModel<P, V> invokeModel, int limit) {
        if (invokeModel == null || CollectionUtil


                .isEmpty(invokeModel.getRows()) || ArrayUtils.isEmpty(invokeModel.getIds())) {
            return;
        }
        P[] ids = invokeModel.getIds();
        int size = ids.length;
        if (size > limit) {
            int count = size / limit;
            if (size % limit != 0) {
                count += 1;
            }
            int length = 0;
            for (int i = 0; i < count; i++) {
                if (i == count - 1) {
                    limit = size - (i * limit);
                }
                P[] subIds = Arrays.copyOfRange(ids, length, length + limit);
                length += limit;
                invokeModel.setIds(subIds);
                if (ArrayUtils.isEmpty(invokeModel.getIds())) {
                    break;
                }
                invoke.accept(invokeModel);
            }
        } else {
            invoke.accept(invokeModel);
        }
    }

    /**
     * 分批请求实体
     *
     * @param <P> id
     * @param <V> 结果
     */
    public static class InvokeModel<P, V> {
        /**
         * id数组
         */
        private P[] ids;
        /**
         * 结果集
         */
        private List<V> rows;

        public P[] getIds() {
            return ids;
        }

        public void setIds(P[] ids) {
            this.ids = ids;
        }

        public List<V> getRows() {
            return rows;
        }

        public void setRows(List<V> rows) {
            this.rows = rows;
        }
    }


    /**
     * 分批获取结果集
     *
     * @param params
     * @param current 当前页
     * @param size    每页长度
     * @param <T>
     * @return
     */
    public static <T> List<T> partGetList(List<T> params, int current, int size) {

        if (CollectionUtil.isEmpty(params)) {
            return new ArrayList<>();
        }

        int startIndex = current * size;
        int endIndex = (current + 1) * size;

        if (params.size() < endIndex) {
            endIndex = params.size();
        }
        if (params.size() < startIndex) {
            return new ArrayList<>();
        }
        return params.subList(startIndex, endIndex);
    }


    /**
     * 分批获取结果集
     *
     * @param invoke 执行函数
     * @param params 参数集合
     * @param limit  一批最大查询的次数
     * @param <P>    参数
     * @param <K>    Map的key
     * @param <V>    Map的vlaue
     * @return 结果集
     */
    public static <P, K, V> Map<K, V> partGetMap(Function<List<P>, Map<K, V>> invoke, List<P> params, int limit) {

        if (CollectionUtil


                .isEmpty(params)) {
            return new HashMap<>(2);
        }

        Map<K, V> map = new HashMap<>(params.size());
        List<P> tmpParams;
        int startIndex = 0;
        int endIndex = params.size();
        while (startIndex < endIndex) {
            int tmpIndex = Math.min((startIndex + limit), endIndex);
            tmpParams = params.subList(startIndex, tmpIndex);
            startIndex += limit;

            Map<K, V> tmp = invoke.apply(tmpParams);
            if (CollectionUtil


                    .isNotEmpty(tmp)) {
                map.putAll(tmp);
            }
        }
        return map;
    }


    /**
     * 分批获取结果集
     *
     * @param invoke 执行函数
     * @param ids    id集合
     * @param limit  一批最大查询的次数
     * @param <P>    id
     * @param <V>    结果
     * @return 结果集
     */
    public static <P, Q, V> List<V> partGetByBiFunc(BiFunction<List<P>, Q[], List<V>> invoke, List<P> ids, Q[] params, int limit) {
        if (CollectionUtil


                .isEmpty(ids)) {
            return new ArrayList<>();
        }
        int size = ids.size();
        List<V> list = new ArrayList<>();
        List<P> subIds;
        if (size > limit) {
            int count = size / limit;
            if (size % limit != 0) {
                count += 1;
            }
            int length = 0;
            for (int i = 0; i < count; i++) {
                if (i == count - 1) {
                    limit = size - (i * limit);
                }
                subIds = ids.subList(length, length + limit);
                length += limit;
                if (CollectionUtil.isEmpty(subIds)) {
                    break;
                }
                List subTemp = invoke.apply(subIds, params);
                if (subTemp != null) {
                    list.addAll(subTemp);
                }
            }
        } else {
            List tmp = invoke.apply(ids, params);
            if (tmp != null) {
                list.addAll(tmp);
            }
        }
        return list;
    }

    // ToDo test
    public static void main(String[] args) {
        ArrayList<Integer> integers = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        List<String> strings = partToList(e -> {
            return e.stream().map(String::valueOf).collect(Collectors.toList());
        }, integers, 2);

        System.out.println("integers" + integers);
        System.out.println("strings" + strings);
    }


}
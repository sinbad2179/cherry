package cn.cherry.core.constant;

/**
 * 日期格式常量
 *
 * @since 2022-01-14
 */
public interface DatePattern {

    /**
     * 年-月-日
     */
    String yyyyMMdd = "yyyyMMdd";

    /**
     * 年-月-日
     */
    String yyyy_MM_dd = "yyyy-MM-dd";

    /**
     * 年-月-日 时:分
     */
    String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";

    /**
     * 年-月-日 时:分:秒
     */
    String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    /**
     * 年-月-日 时:分:秒:毫秒
     */
    String yyyy_MM_dd_HH_mm_ss_S = "yyyy-MM-dd HH:mm:ss.S";

    /**
     * 年月日时分秒
     */
    String yyyyMMddHHmmss = "yyyyMMddHHmmss";

    /**
     * 时:分
     */
    String HH_mm = "HH:mm";

    /**
     * 时:分:秒
     */
    String HH_mm_ss = "HH:mm:ss";

    /**
     * 时:分:秒:毫秒
     */
    String HH_mm_ss_S = "HH:mm:ss:S";

}
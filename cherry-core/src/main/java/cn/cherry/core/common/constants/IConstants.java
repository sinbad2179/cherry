package cn.cherry.core.common.constants;

/**
 * @author: sinbad.cheng
 * @time: 2023/2/26 17:59
 * @description： 通用常量类
 */
public interface IConstants {

    /*系统状态*/

    /**
     * 启用
     */
    Integer ENABLE = 1;
    /**
     * 未启用
     */
    Integer UN_ENABLE = 0;

    /*调用链*/

    /**
     * 链路ID
     */
    String TRACE_ID = "traceId";

    /**
     * web服务通用常量
     */
    class WebConstant {

        /**
         * 应用名称请求头
         */
        public final static String APPLICATION_NAME_HEADER = "x-application-name";

        /**
         * 请求开始时间
         */
        public final static String REQUEST_START_TIME = "_REQUEST_START_TIME";

        /**
         * 请求异常
         */
        public final static String EXCEPTION = "_REQUEST_EXCEPTION";

        /**
         * 返回数据
         */
        public final static String RESPONSE_DATA = "_RESPONSE_DATA";

    }

}

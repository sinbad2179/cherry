package cn.cherry.rpc.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author: sinbad.cheng
 * @time: 2023/3/1 21:13
 * @description： 请求日志模型
 */
@Data
@Accessors(chain = true)
public class TraceLog implements Serializable {
    private static final long serialVersionUID = 4647765008396784808L;

    /**
     * 应用端口号
     */
    private String serverPort;
    /**
     * 应用名称
     */
    private String serverName;
    private String method;
    private String uri;
    /**
     * 请求头
     */
    private Map<String, String> headers;
    /**
     * 请求参数
     */
    private String requestParam;
    /**
     * 响应体
     */
    private String requestBody;
    private String respData;

    /**
     * 追踪日志ID
     */
    private String traceId;
    private String status;
    private LocalDateTime reqTime;
    private LocalDateTime respTime;
    /**
     * 耗时（毫秒）
     */
    private Long duration;


}

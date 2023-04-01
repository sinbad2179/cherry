package cn.cherry.rpc.feign;

import cn.cherry.core.model.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: sinbad.cheng
 * @time: 2023/2/27 20:28
 * @description：
 */
public abstract class AbstractFeignClient<REQ, RESP extends Result<?>> implements FeignClient<REQ, RESP> {

    /**
     * RPC路径目录
     */
    protected static Map<String, List<String>> CONTENT_GROUP = new HashMap<>();

    /**
     * 加入CONTENT
     */
    public abstract void register();

    /**
     * 请求调用方法
     *
     * @param request
     * @return
     */
    @Override
    public RESP execute(REQ request, String path) {
        preCheckExecute(request);
        return handler(request);
    }

    /**
     * 校验
     *
     * @param request
     */
    public void preCheckExecute(Object request) {
        // check
    }


    /**
     * 具体执行类
     *
     * @param req
     * @return
     */
    public abstract RESP handler(REQ req);
}

package cn.cherry.rpc.feign;

import cn.cherry.core.model.Result;

/**
 * @author: sinbad.cheng
 * @time: 2023/2/27 20:07
 * @description： Feign调用接口
 */
public interface FeignClient<REQ, RESP extends Result<?>> {


    /**
     * 请求调用方法
     *
     * @param request
     * @return
     */
    RESP execute(REQ request, String path);

}

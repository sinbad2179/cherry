package cn.cherry.rpc.feign;

import cn.cherry.core.model.Result;

/**
 * @author: sinbad.cheng
 * @time: 2023/2/27 20:28
 * @description：
 */
public abstract class AbstractFeignClient<REQ, RESP extends Result> implements FeignClient<REQ, RESP> {


    /**
     * 请求调用方法
     *
     * @param request
     * @return
     */
    @Override
    public RESP execute(REQ request) {
        preCheckExecute(request);
        return null;
    }

    /**
     * 校验
     *
     * @param request
     */
    public void preCheckExecute(Object request) {

    }
}

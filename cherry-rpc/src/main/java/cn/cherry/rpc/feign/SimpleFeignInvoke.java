package cn.cherry.rpc.feign;

import cn.cherry.core.model.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author: sinbad.cheng
 * @time: 2023/2/27 20:32
 * @description： feign测试
 * ToDo
 */
@Component
public interface SimpleFeignInvoke extends FeignClient<String, Result<Boolean>> {




    /**
     * 测试feign调用
     *
     * @param reqDto
     * @return
     */
    @PostMapping("serviceA/list")
    Result<Void> list(String reqDto);

}

package cn.cherry.rpc.web.config;

import cn.cherry.rpc.web.interceptor.GateInterceptor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;

/**
 * @author: sinbad.cheng
 * @time: 2023/2/26 20:48
 * @description： spring web配置
 */
@Configurable
public class WebAutoConfiguration implements WebMvcConfigurer {


    /**
     * Add {@link Converter Converters} and {@link Formatter Formatters} in addition to the ones
     * registered by default.
     * <p>
     * 作用域：非JSON（@RequestBody）的请求
     *
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar formatterRegistrar = new DateTimeFormatterRegistrar();
        formatterRegistrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        formatterRegistrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        formatterRegistrar.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm:ss"));
        formatterRegistrar.registerFormatters(registry);
    }

    /**
     * Add Spring MVC lifecycle interceptors for pre- and post-processing of
     * controller method invocations and resource handler requests.
     * Interceptors can be registered to apply to all requests or be limited
     * to a subset of URL patterns.
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getInterceptor()).addPathPatterns("/**").excludePathPatterns();
    }


    /**
     * 获取日志打印拦截器
     *
     * @return
     */
    public GateInterceptor getInterceptor() {
        return new GateInterceptor();
    }
}

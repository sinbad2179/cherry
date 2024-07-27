package cn.cherry.rpc.web.config;

import cn.cherry.rpc.web.advice.ResultResponseAdvice;
import cn.cherry.rpc.web.config.properties.WebRequestProperties;
import cn.cherry.rpc.web.interceptor.GateInterceptor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.time.format.DateTimeFormatter;

/**
 * @author: sinbad.cheng
 * @time: 2023/2/26 20:48
 * @description： spring web配置
 */
@Configurable
@EnableConfigurationProperties(WebRequestProperties.class)
public class WebAutoConfiguration implements WebMvcConfigurer {


    /**
     * 自定义请求配置
     */
    private final WebRequestProperties webRequestProperties;

    /**
     * 构造函数
     *
     * @param webRequestProperties
     */
    public WebAutoConfiguration(WebRequestProperties webRequestProperties) {
        this.webRequestProperties = webRequestProperties;
    }

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
     * 处理HTTP返回结果
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public ResultResponseAdvice resultResponseAdvice() {
        return new ResultResponseAdvice();
    }

    /**
     * 创建 CorsFilter 解决跨域问题
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOrigin("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 创建 UrlBasedCorsConfigurationSource 对象
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对接口配置跨域设置
        source.registerCorsConfiguration("/**", config);
        return createFilterBean(new CorsFilter(source), Ordered.HIGHEST_PRECEDENCE);
    }


    private static <T extends Filter> FilterRegistrationBean<T> createFilterBean(T filter, Integer order) {
        FilterRegistrationBean<T> bean = new FilterRegistrationBean<>(filter);
        bean.setOrder(order);
        return bean;
    }

    /**
     * 获取日志打印拦截器
     *
     * @return
     */
    public GateInterceptor getInterceptor() {
        return new GateInterceptor(webRequestProperties);
    }
}

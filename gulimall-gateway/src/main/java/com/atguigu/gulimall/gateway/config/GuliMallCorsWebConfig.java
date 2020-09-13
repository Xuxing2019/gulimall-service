package com.atguigu.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author xuxing
 * @date 2020/6/29
 */
@Configuration
public class GuliMallCorsWebConfig {
    /**
     * 设置跨域配置
     * @return
     */
    @Bean
    public CorsWebFilter corsWebFilter(){
        //CorsConfigurationSource配置跨域资源设置 UrlBasedCorsConfigurationSource(子类)
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //允许哪些头进行跨域
        corsConfiguration.addAllowedHeader("*");
        //允许哪些请求方式进行跨域
        corsConfiguration.addAllowedMethod("*");
        //允许哪些请求来源进行跨域
        corsConfiguration.addAllowedOrigin("*");
        //允许携带Cookie进行跨域
        corsConfiguration.setAllowCredentials(true);
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        //创建跨域过滤器加入Spring容器
        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }
}

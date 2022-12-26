package com.company.project.configurer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/****
 * 实现跨域访问，前端无需再配置跨域
 */
@Configuration
public class CorsConfig {

    @Value("${custom.cross.domain}")
    private boolean crossDomain;

    // 当前跨域请求最大有效时长。这里默认1天
    private static final long MAX_AGE = 24 * 60 * 60;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        if (crossDomain) {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.addAllowedOrigin("*"); // 1 设置访问源地址
            corsConfiguration.addAllowedHeader("*"); // 2 设置访问源请求头
            corsConfiguration.addAllowedMethod("*"); // 3 设置访问源请求方法
            corsConfiguration.setMaxAge(MAX_AGE);
            source.registerCorsConfiguration("/**", corsConfiguration); // 4 对接口配置跨域设置
        }
        return new CorsFilter(source);
    }
}
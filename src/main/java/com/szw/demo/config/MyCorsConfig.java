package com.szw.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author Szw 2001
 * @Date 2023/3/15 20:13
 * @Slogn 致未来的你！
 */
@Configuration
public class MyCorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        //cors配置
        CorsConfiguration configuration = new CorsConfiguration();
        //前端服务器ip
        configuration.addAllowedOrigin("http://localhost:8888");
        //是否允许发送cookie
        configuration.setAllowCredentials(true);
        configuration.addAllowedMethod("*");
        //允许发送头信息
        configuration.addAllowedHeader("*");

        //映射路径 拦截请求
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",configuration);


        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}

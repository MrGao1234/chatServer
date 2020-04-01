package com.example.chatserver.config;

import com.example.chatserver.interceptor.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class CorsConfiger extends WebMvcConfigurationSupport {

    //注册拦截器
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor( new UserInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/registerAndLogin/**");//放过 registerAndLogin 路径下的接口
    }

}

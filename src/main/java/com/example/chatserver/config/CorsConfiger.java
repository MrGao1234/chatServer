package com.example.chatserver.config;

import com.example.chatserver.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
public class CorsConfiger extends WebMvcConfigurationSupport {

    @Value("${file.basedir}")
    private String fileUrl;

    //注册拦截器
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor( new UserInterceptor())
                .addPathPatterns("/**");
                //.excludePathPatterns("/registerAndLogin/**");//放过 registerAndLogin 路径下的接口
    }

    /*
        编码
    */
    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
        addDefaultHttpMessageConverters(converters);
    }
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }


    /*静态资源映射*/
    /*
     * 功能描述:
     *  配置静态资源,避免静态资源请求被拦截
     * */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println(fileUrl);
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:resources/", "classpath:static/",
                        "classpath:public/", "classpath:META-INF/resources/")
                .addResourceLocations(fileUrl);
    }

}

package com.xz;

import com.xz.controller.interceptor.MiniInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author: huangfurong
 * @Description:
 * @Date: Create in 22:53 2019-07-03
 */
@Configuration
public class webMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:META-INF/resources/")
                .addResourceLocations("file:F:/xz_fdy_db/");
    }

    @Bean
    public MiniInterceptor miniInterceptor(){
        return  new MiniInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(miniInterceptor()).addPathPatterns("/user/**")
                                                  .addPathPatterns("/bgm/**")
                                                  .addPathPatterns("/video/upload","/video/uploadCover")
                                                  .addPathPatterns("/video/userLikeVideo","/video/userUnLikeVideo",
                                                          "/video/saveComment")
                                                  .excludePathPatterns("/user/queryPublisher")
                                                  .excludePathPatterns("/user/login");

        super.addInterceptors(registry);
    }
}

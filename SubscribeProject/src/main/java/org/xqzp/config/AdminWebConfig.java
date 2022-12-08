package org.xqzp.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.xqzp.interceptor.KeyInterceptor;
import org.xqzp.interceptor.TokenInterceptor;

@Configuration
public class AdminWebConfig implements WebMvcConfigurer {
    @Bean
    public TokenInterceptor tokenInterceptor(){
        return new TokenInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor())
                .addPathPatterns("/v2/**")
                .addPathPatterns("/yaml/**");

        registry.addInterceptor(new KeyInterceptor())
                .addPathPatterns("/user/**");
    }
}

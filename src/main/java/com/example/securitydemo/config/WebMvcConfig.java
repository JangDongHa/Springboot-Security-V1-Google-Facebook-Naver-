package com.example.securitydemo.config;

import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        MustacheViewResolver resolver = new MustacheViewResolver(); // 뷰 리졸버 기본 설정이 머스태치로 되어있기 때문에 
        resolver.setCharset("UTF-8");
        resolver.setContentType("text/html; charset=UTF-8");
        resolver.setPrefix("classpath:/templates/"); // classpath: 은 내 프로젝트 경로
        resolver.setSuffix(".html"); // 설정을 html로 바꿔주고

        registry.viewResolver(resolver); // 뷰리졸버 레지스트리에 등록
    }

}

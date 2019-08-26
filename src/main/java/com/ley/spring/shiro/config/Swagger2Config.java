package com.ley.spring.shiro.config;

import com.ley.spring.shiro.utils.SwaggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2 com.wx.tourism.config
 **/
@Configuration
@Slf4j
@EnableSwagger2
public class Swagger2Config implements EnvironmentAware {

    private static final String SWAGGER_PREFIX = "spring.swagger.";


    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment environment) {
        propertyResolver = new RelaxedPropertyResolver(environment, Swagger2Config.SWAGGER_PREFIX);
    }


    @Bean
    public Docket docket() {
        SwaggerUtils.setTitle(propertyResolver.getProperty("title"));
        SwaggerUtils.setVersion(propertyResolver.getProperty("version"));
        log.info("title: {},version: {}", propertyResolver.getProperty("title"), propertyResolver.getProperty("version"));
        return SwaggerUtils.initDocket();
    }
}

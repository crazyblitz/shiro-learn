package com.ley.spring.shiro.utils;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * swagger utility class
 **/
public class SwaggerUtils {

    /**
     * swagger api title
     **/
    private static String title;

    /**
     * swagger api version
     **/
    private static String version;


    private static ApiInfo apiInfo() {
        return (new ApiInfoBuilder()).title(SwaggerUtils.title).version(SwaggerUtils.version).build();
    }

    public static Docket initDocket() {
        return (new Docket(DocumentationType.SWAGGER_2)).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()).build();
    }

    public static void setTitle(String title) {
        SwaggerUtils.title = title;
    }

    public static void setVersion(String version) {
        SwaggerUtils.version = version;
    }
}
package com.ley.spring.shiro.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import com.ley.spring.shiro.exception.ServiceException;
import com.ley.spring.shiro.utils.web.result.Result;
import com.ley.spring.shiro.utils.web.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        //解决中文乱码问题
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();

        //增加时间格式化的序列化
        config.setSerializerFeatures(SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteDateUseDateFormat);
        // 按需配置,更多参考FastJson文档
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        converters.add(converter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedHeaders("*")
                .maxAge(-1);
    }


    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add((request, response, handler, e) -> {
            Result result = new Result();
            HandlerMethod method = (HandlerMethod) handler;
            log.error("接口: {} 出现异常,类: {},方法: {},异常摘要: {}",
                    request.getRequestURI(), method.getBean().getClass().getName(), method.getMethod().getName(),
                    e.getMessage());
            if (e instanceof ServiceException) {//业务失败的异常,如"账号或密码错误"
                result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                log.error(e.getMessage());
            } else if (e instanceof NoHandlerFoundException) {
                result.setCode(ResultCode.NOT_FOUND).setMessage("接口 [" + request.getRequestURI() + "] 不存在");
                log.error(e.getMessage());
            } else if (e instanceof ServletException) {
                result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                log.error(e.getMessage());

            }
            //TODO:  如果用户没有验证,要跳转的页面
            else if (e instanceof UnauthenticatedException) {
                result.setCode(ResultCode.UNAUTHORIZED).setMessage("用户没有经过验证,需要进行登录").setData(false);
                log.error(e.getMessage());
            } else if (e instanceof UnauthorizedException) {
                result.setCode(ResultCode.UNAUTHORIZED).setMessage("用户没有访问" + "接口[" + request.getRequestURI() + "]角色或者权限").setData(false);
                log.error(e.getMessage());
            } else {
                result.setCode(ResultCode.INTERNAL_SERVER_ERROR).setMessage("接口 [" + request.getRequestURI() + "] 内部错误，请联系管理员");
                String message;
                if (handler instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    message = String.format("接口 [%s] 出现异常，方法：[%s.%s]，异常摘要：%s",
                            request.getRequestURI(),
                            handlerMethod.getBean().getClass().getName(),
                            handlerMethod.getMethod().getName(),
                            e.getMessage());
                } else {
                    message = e.getMessage();
                }
                // result.setMessage(message);
                log.error(message, e);
            }
            responseResult(response, result);
            return new ModelAndView();
        });
    }


    private void responseResult(HttpServletResponse response, Result result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            log.error(ex.getMessage(),ex);
        }
    }
}

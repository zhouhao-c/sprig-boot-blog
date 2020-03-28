package com.chinasoft.blog.config;

import com.chinasoft.blog.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/admin/**")
        .excludePathPatterns("/admin").excludePathPatterns("/admin/login");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //浏览器发送/hao请求到successye页面
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/index").setViewName("admin/adminIndex");
        registry.addViewController("/demo").setViewName("admin/demo");
    }

    @Bean
    public LocaleResolver localeResolver(){
        return new MyLocaleResolver();
    }

    public class MyLocaleResolver implements LocaleResolver {

        @Override
        public Locale resolveLocale(HttpServletRequest request) {
            String l =request.getParameter("l");
            Locale locale = Locale.getDefault();
            if (!StringUtils.isEmpty(l))
            {
                String[] split = l.split("_");
                locale = new Locale(split[0],split[1]);

            }
            return locale;
        }

        @Override
        public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

        }
    }
}

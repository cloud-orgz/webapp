package com.csye6225.assignment1.config;

import com.csye6225.assignment1.filter.NoAuthForOpenUrlsFilter;
import com.csye6225.assignment1.filter.NoQueryParamFilter;
import com.csye6225.assignment1.filter.NoRequestBodyFilter;
import com.csye6225.assignment1.filter.ResponseFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<ResponseFilter> customFilter() {
        FilterRegistrationBean<ResponseFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ResponseFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<NoRequestBodyFilter> noRequestBodyFilter() {
        FilterRegistrationBean<NoRequestBodyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new NoRequestBodyFilter());
        registrationBean.addUrlPatterns("/healthz"); 
        registrationBean.setOrder(3);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<NoQueryParamFilter> noQueryParamFilter() {
        FilterRegistrationBean<NoQueryParamFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new NoQueryParamFilter());
        registrationBean.addUrlPatterns("/*"); 
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<NoAuthForOpenUrlsFilter> noAuthForOpenUrlsFilter() {
        FilterRegistrationBean<NoAuthForOpenUrlsFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new NoAuthForOpenUrlsFilter());
        registrationBean.addUrlPatterns("/healthz");
        registrationBean.addUrlPatterns("/v1/user");
        registrationBean.setOrder(4);
        return registrationBean;
    }
}

package com.ecommerce.api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration

public class CacheConfig {
    @Bean
    public FilterRegistrationBean<ShallowEtagHeaderFilter> etagFilter() {
        FilterRegistrationBean<ShallowEtagHeaderFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ShallowEtagHeaderFilter());
        bean.addUrlPatterns("/api/v1/*");
        return bean;
    }
}

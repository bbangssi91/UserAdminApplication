package com.autoever.useradminapplication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PageableConfig implements PageableHandlerMethodArgumentResolverCustomizer {

    @Override
    public void customize(PageableHandlerMethodArgumentResolver pageableResolver) {
        // 1페이지부터 시작하도록 설정
        pageableResolver.setOneIndexedParameters(true);
        // 최대 페이지 크기 제한 설정 (예: 300)
        pageableResolver.setMaxPageSize(300);
    }
}
package project.tosstock.common.config.web;

import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.config.web.filter.JwtExceptionFilter;
import project.tosstock.common.config.web.filter.JwtVerificationFilter;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

	private final JwtVerificationFilter jwtVerificationFilter;
	private final JwtExceptionFilter jwtExceptionFilter;

	private final List<String> jwtUrls = List.of("/api/v1/logout**", "/api/v1/member/*");

	@Bean
	public FilterRegistrationBean<JwtExceptionFilter> jwtExceptionFilterBean() {
		FilterRegistrationBean<JwtExceptionFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();

		filterFilterRegistrationBean.setFilter(jwtExceptionFilter);
		filterFilterRegistrationBean.setOrder(1);
		filterFilterRegistrationBean.setUrlPatterns(jwtUrls);

		return filterFilterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<JwtVerificationFilter> jwtVerificationFilterBean() {
		FilterRegistrationBean<JwtVerificationFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();

		filterFilterRegistrationBean.setFilter(jwtVerificationFilter);
		filterFilterRegistrationBean.setOrder(2);
		filterFilterRegistrationBean.setUrlPatterns(jwtUrls);

		return filterFilterRegistrationBean;
	}
}

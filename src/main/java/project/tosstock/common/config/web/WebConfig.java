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

	@Bean
	public FilterRegistrationBean<JwtExceptionFilter> jwtFailAuthenticationFilter() {
		FilterRegistrationBean<JwtExceptionFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();

		filterFilterRegistrationBean.setFilter(jwtExceptionFilter);
		filterFilterRegistrationBean.setOrder(1);
		filterFilterRegistrationBean.setUrlPatterns(List.of("/*"));

		return filterFilterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<JwtVerificationFilter> authFilter() {
		FilterRegistrationBean<JwtVerificationFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();

		filterFilterRegistrationBean.setFilter(jwtVerificationFilter);
		filterFilterRegistrationBean.setOrder(2);
		filterFilterRegistrationBean.setUrlPatterns(List.of("/api/v1/logout**", "/api/v1/member/*"));

		return filterFilterRegistrationBean;
	}
}

package project.tosstock.common.config.web;

import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import project.tosstock.common.config.web.filter.JwtAuthenticationFilter;
import project.tosstock.common.config.web.filter.JwtFilter;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

	private final JwtFilter jwtFilter;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public FilterRegistrationBean<JwtFilter> authFilter() {
		FilterRegistrationBean<JwtFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();

		filterFilterRegistrationBean.setFilter(jwtFilter);
		filterFilterRegistrationBean.setOrder(2);
		filterFilterRegistrationBean.setUrlPatterns(List.of("/api/v1/logout**", "/api/v1/member/*"));

		return filterFilterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> jwtFailAuthenticationFilter() {
		FilterRegistrationBean<JwtAuthenticationFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();

		filterFilterRegistrationBean.setFilter(jwtAuthenticationFilter);
		filterFilterRegistrationBean.setOrder(1);
		filterFilterRegistrationBean.setUrlPatterns(List.of("/*"));

		return filterFilterRegistrationBean;
	}
}

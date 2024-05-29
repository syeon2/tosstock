package project.tosstock.common.config.web.filter;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import project.tosstock.common.jwt.JwtTokenProvider;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	@Value("${jwt.header}")
	private String header;

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		jwtTokenProvider.verifyToken(request.getHeader(header));
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String[] excludePath = {"/api/v1/member/*", "/api/v1/login", "/api/v1/auth/*"};
		String path = request.getRequestURI();

		return Arrays.stream(excludePath).anyMatch(path::startsWith);
	}
}

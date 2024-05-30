package project.tosstock.common.config.web.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import project.tosstock.common.wrapper.ApiResult;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain chain) throws ServletException, IOException {

		try {
			chain.doFilter(request, response);
		} catch (ExpiredJwtException exception) {
			expirationJwtException(response);
		} catch (RuntimeException exception) {
			wrongJwtTokenException(response);
		}
	}

	private void wrongJwtTokenException(HttpServletResponse response) throws IOException {
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setContentType(MediaType.APPLICATION_JSON.toString());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.getWriter()
			.write(objectMapper.writeValueAsString(ApiResult.error(HttpStatus.BAD_REQUEST, "잘못된 토큰입니다.")));
	}

	private void expirationJwtException(HttpServletResponse response) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON.toString());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.getWriter()
			.write(objectMapper.writeValueAsString(ApiResult.error(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.")));
	}
}

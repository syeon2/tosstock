package project.tosstock.common.config.web.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import project.tosstock.common.error.exception.UnAuthenticationTokenException;
import project.tosstock.common.wrapper.ApiResult;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain chain) throws ServletException, IOException {

		try {
			chain.doFilter(request, response);
		} catch (UnAuthenticationTokenException exception) {
			unAuthenticationException(response, exception.getMessage());
		} catch (RuntimeException exception) {
			wrongJwtTokenException(response, exception.getMessage());
		}
	}

	private void wrongJwtTokenException(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setContentType(MediaType.APPLICATION_JSON.toString());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.getWriter()
			.write(objectMapper.writeValueAsString(ApiResult.error(HttpStatus.BAD_REQUEST, message)));
	}

	private void unAuthenticationException(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON.toString());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.getWriter()
			.write(objectMapper.writeValueAsString(ApiResult.error(HttpStatus.UNAUTHORIZED, message)));
	}
}

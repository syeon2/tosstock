package project.tosstock.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import project.tosstock.common.error.exception.UnAuthenticationTokenException;
import project.tosstock.common.wrapper.ApiResult;

@RestControllerAdvice
public class ErrorController {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ApiResult<Void> handlerValidationRequest(MethodArgumentNotValidException exception) {
		return ApiResult.error(HttpStatus.BAD_REQUEST,
			exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnAuthenticationTokenException.class)
	public ApiResult<Void> handlerExpiredJwtRequest(UnAuthenticationTokenException exception) {
		return ApiResult.error(HttpStatus.UNAUTHORIZED, exception.getLocalizedMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(RuntimeException.class)
	public ApiResult<Void> handlerBadRequest(RuntimeException exception) {
		return ApiResult.error(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage());
	}
}

package project.tosstock.common.wrapper;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResult<T> {

	private Integer status;
	private String message;
	private T data;

	@Builder
	private ApiResult(Integer status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public static <T> ApiResult<T> of(HttpStatus status, String message, T data) {
		return ApiResult.<T>builder()
			.status(status.value())
			.message(message)
			.data(data)
			.build();
	}

	public static <T> ApiResult<T> ok(T data) {
		return of(HttpStatus.OK, null, data);
	}

	public static <T> ApiResult<T> error(HttpStatus status, String message) {
		return of(status, message, null);
	}
}

package project.tosstock.activity.adapter.in.web.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasicActivityResponse<T> {

	private T result;

	public BasicActivityResponse(T result) {
		this.result = result;
	}

	public static <T> BasicActivityResponse<T> of(T result) {
		return new BasicActivityResponse<>(result);
	}
}

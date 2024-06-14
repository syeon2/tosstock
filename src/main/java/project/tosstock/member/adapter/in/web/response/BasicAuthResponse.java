package project.tosstock.member.adapter.in.web.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasicAuthResponse<T> {

	private T result;

	private BasicAuthResponse(T result) {
		this.result = result;
	}

	public static <T> BasicAuthResponse<T> of(T result) {
		return new BasicAuthResponse<>(result);
	}
}

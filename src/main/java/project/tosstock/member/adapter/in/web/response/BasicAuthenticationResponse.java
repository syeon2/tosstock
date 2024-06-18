package project.tosstock.member.adapter.in.web.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasicAuthenticationResponse<T> {

	private T result;

	private BasicAuthenticationResponse(T result) {
		this.result = result;
	}

	public static <T> BasicAuthenticationResponse<T> of(T result) {
		return new BasicAuthenticationResponse<>(result);
	}
}

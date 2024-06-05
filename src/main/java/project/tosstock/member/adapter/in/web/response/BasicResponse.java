package project.tosstock.member.adapter.in.web.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasicResponse {

	private boolean result;

	private BasicResponse(boolean result) {
		this.result = result;
	}

	public static BasicResponse of(boolean result) {
		return new BasicResponse(result);
	}
}

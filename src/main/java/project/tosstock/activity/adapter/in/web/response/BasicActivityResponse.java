package project.tosstock.activity.adapter.in.web.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasicActivityResponse {

	private Long id;

	public BasicActivityResponse(Long id) {
		this.id = id;
	}

	public static BasicActivityResponse of(Long id) {
		return new BasicActivityResponse(id);
	}
}

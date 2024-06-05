package project.tosstock.member.adapter.in.web.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinMemberResponse {

	private Long memberId;

	private JoinMemberResponse(Long memberId) {
		this.memberId = memberId;
	}

	public static JoinMemberResponse of(Long memberId) {
		return new JoinMemberResponse(memberId);
	}
}

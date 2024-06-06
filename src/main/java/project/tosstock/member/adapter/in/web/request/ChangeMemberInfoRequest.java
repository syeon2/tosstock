package project.tosstock.member.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.tosstock.member.application.domain.model.UpdateMemberDto;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeMemberInfoRequest {

	@NotBlank(message = "이름은 빈칸을 허용하지 않습니다.")
	private String username;

	@NotNull(message = "자기소개는 null을 허용하지 않습니다.")
	private String introduce;

	private String profileImageUrl;

	@Builder
	private ChangeMemberInfoRequest(String username, String introduce, String profileImageUrl) {
		this.username = username;
		this.introduce = introduce;
		this.profileImageUrl = profileImageUrl;
	}

	public UpdateMemberDto toServiceDto() {
		return UpdateMemberDto.builder()
			.username(this.username)
			.profileImageUrl(this.profileImageUrl)
			.introduce(this.introduce)
			.build();
	}
}

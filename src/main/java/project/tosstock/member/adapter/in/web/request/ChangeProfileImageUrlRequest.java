package project.tosstock.member.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeProfileImageUrlRequest {

	@NotBlank(message = "프로필 이미지 URL은 필수 값입니다.")
	private String profileImageUrl;

	public ChangeProfileImageUrlRequest(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
}

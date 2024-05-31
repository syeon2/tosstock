package project.tosstock.common.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtPayloadDto {

	private String email;
	private String address;

	@Builder
	private JwtPayloadDto(String email, String address) {
		this.email = email;
		this.address = address;
	}
}

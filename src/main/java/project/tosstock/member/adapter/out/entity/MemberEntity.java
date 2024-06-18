package project.tosstock.member.adapter.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.tosstock.common.wrapper.BaseEntity;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id", columnDefinition = "bigint")
	private Long id;

	@Column(name = "username", columnDefinition = "varchar(60)", nullable = false)
	private String username;

	@Column(name = "email", unique = true, columnDefinition = "varchar(60)", nullable = false)
	private String email;

	@Column(name = "password", columnDefinition = "char(60)", nullable = false)
	private String password;

	@Column(name = "phone_number", unique = true, columnDefinition = "varchar(30)", nullable = false)
	private String phoneNumber;

	@Lob
	@Column(name = "introduce", columnDefinition = "text")
	private String introduce;

	@Column(name = "profile_image_url", columnDefinition = "varchar(255)")
	private String profileImageUrl;

	@Builder
	private MemberEntity(Long id, String username, String email, String password, String phoneNumber, String introduce,
		String profileImageUrl) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.introduce = introduce;
		this.profileImageUrl = profileImageUrl;
	}
}

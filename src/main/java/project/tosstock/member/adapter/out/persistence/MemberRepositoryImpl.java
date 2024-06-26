package project.tosstock.member.adapter.out.persistence;

import static project.tosstock.member.adapter.out.entity.QMemberEntity.*;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import lombok.RequiredArgsConstructor;
import project.tosstock.member.application.domain.model.UpdateMemberDto;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Transactional
	@Override
	public Optional<String> findPasswordByEmail(String email) {
		String password = queryFactory
			.select(memberEntity.password)
			.from(memberEntity)
			.where(memberEntity.email.eq(email))
			.fetchOne();

		return Optional.ofNullable(password);
	}

	@Transactional
	@Override
	public Optional<String> findUsernameById(Long memberId) {
		String username = queryFactory
			.select(memberEntity.username)
			.from(memberEntity)
			.where(memberEntity.id.eq(memberId))
			.fetchOne();

		return Optional.ofNullable(username);
	}

	@Transactional
	@Modifying(clearAutomatically = true)
	@Override
	public void updateInfo(Long memberId, UpdateMemberDto updateMemberDto) {
		JPAUpdateClause update = queryFactory.update(memberEntity);

		setNotNullFields(update, memberEntity.username, updateMemberDto.getUsername());
		setNotNullFields(update, memberEntity.introduce, updateMemberDto.getIntroduce());

		update
			.set(memberEntity.profileImageUrl, updateMemberDto.getProfileImageUrl())
			.where(memberEntity.id.eq(memberId))
			.execute();
	}

	@Transactional
	@Modifying(clearAutomatically = true)
	@Override
	public void updatePassword(String email, String password) {
		queryFactory.update(memberEntity)
			.set(memberEntity.password, password)
			.where(memberEntity.email.eq(email))
			.execute();
	}

	private void setNotNullFields(JPAUpdateClause update, StringPath field, String value) {
		if (value != null) {
			update.set(field, value);
		}
	}
}

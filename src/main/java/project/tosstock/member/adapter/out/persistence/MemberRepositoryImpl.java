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
	@Modifying(clearAutomatically = true)
	@Override
	public void updateInfo(Long memberId, UpdateMemberDto updateMemberDto) {
		JPAUpdateClause update = queryFactory.update(memberEntity);

		setNotNullFields(update, memberEntity.username, updateMemberDto.getUsername());
		setNotNullFields(update, memberEntity.introduce, updateMemberDto.getIntroduce());
		setNotNullFields(update, memberEntity.profileImageUrl, updateMemberDto.getProfileImageUrl());

		update
			.where(memberEntity.id.eq(memberId))
			.execute();
	}

	@Transactional
	@Modifying(clearAutomatically = true)
	@Override
	public void updatePassword(Long memberId, String password) {
		queryFactory.update(memberEntity)
			.set(memberEntity.password, password)
			.where(memberEntity.id.eq(memberId))
			.execute();
	}

	private void setNotNullFields(JPAUpdateClause update, StringPath field, String value) {
		if (value != null) {
			update.set(field, value);
		}
	}
}

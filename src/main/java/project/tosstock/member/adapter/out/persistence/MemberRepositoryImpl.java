package project.tosstock.member.adapter.out.persistence;

import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tosstock.member.adapter.out.entity.QMemberEntity;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<String> findPasswordByEmail(String email) {
		String password = queryFactory
			.select(QMemberEntity.memberEntity.password)
			.from(QMemberEntity.memberEntity)
			.where(QMemberEntity.memberEntity.email.eq(email))
			.fetchOne();

		return Optional.ofNullable(password);
	}
}

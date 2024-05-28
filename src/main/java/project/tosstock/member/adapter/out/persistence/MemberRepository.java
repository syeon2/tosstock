package project.tosstock.member.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.tosstock.member.adapter.out.entity.MemberEntity;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>, MemberRepositoryCustom {

	Optional<MemberEntity> findByEmail(String email);

	Optional<MemberEntity> findByPhoneNumber(String phoneNumber);
}

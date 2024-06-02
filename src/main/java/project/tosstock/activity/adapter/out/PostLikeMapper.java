package project.tosstock.activity.adapter.out;

import org.springframework.stereotype.Component;

import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.adapter.out.entity.PostLikeEntity;
import project.tosstock.member.adapter.out.entity.MemberEntity;

@Component
public class PostLikeMapper {

	public PostLikeEntity toEntity(MemberEntity member, PostEntity post) {
		return PostLikeEntity.builder()
			.member(member)
			.post(post)
			.build();
	}
}

package project.tosstock.activity.adapter;

import org.springframework.stereotype.Component;

import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.application.domain.model.Post;
import project.tosstock.member.adapter.out.entity.MemberEntity;

@Component
public class PostMapper {

	public PostEntity toEntity(MemberEntity member, Post post) {
		return PostEntity.builder()
			.article(post.getArticle())
			.member(member)
			.build();
	}
}

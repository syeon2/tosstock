package project.tosstock.activity.adapter.out;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.adapter.out.persistence.PostRepository;
import project.tosstock.activity.application.domain.model.Post;
import project.tosstock.activity.application.port.out.DeletePostPort;
import project.tosstock.activity.application.port.out.FindPostPort;
import project.tosstock.activity.application.port.out.SavePostPort;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;

@PersistenceAdapter
@RequiredArgsConstructor
public class PostPersistenceAdapter implements SavePostPort, DeletePostPort, FindPostPort {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;

	private final PostMapper postMapper;

	@Override
	@Transactional
	public Long save(Post post) {
		MemberEntity member = memberRepository.findById(post.getMemberId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

		PostEntity savePost = postRepository.save(postMapper.toEntity(member, post));

		return savePost.getId();
	}

	@Override
	public Long delete(Long postId) {
		postRepository.deleteById(postId);

		return postId;
	}

	@Override
	public PostEntity findPostById(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포스트입니다."));
	}
}

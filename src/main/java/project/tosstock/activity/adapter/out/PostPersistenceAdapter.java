package project.tosstock.activity.adapter.out;

import java.util.Optional;

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
	public Long save(Post post) {
		MemberEntity proxyMember = memberRepository.getReferenceById(post.getMemberId());
		PostEntity savedPost = postRepository.save(postMapper.toEntity(proxyMember, post));

		return savedPost.getId();
	}

	@Override
	public void delete(Long postId) {
		postRepository.deleteById(postId);
	}

	@Override
	public Optional<Post> findPostById(Long postId) {
		return postRepository.findById(postId)
			.map(postMapper::toDomain);
	}
}

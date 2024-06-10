package project.tosstock.activity.adapter.out;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.adapter.out.entity.PostLikeEntity;
import project.tosstock.activity.adapter.out.persistence.PostLikeRepository;
import project.tosstock.activity.adapter.out.persistence.PostRepository;
import project.tosstock.activity.application.port.out.DeletePostLikePort;
import project.tosstock.activity.application.port.out.SavePostLikePort;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;

@PersistenceAdapter
@RequiredArgsConstructor
public class PostLikePersistenceAdapter implements SavePostLikePort, DeletePostLikePort {

	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final PostLikeRepository postLikeRepository;

	private final PostLikeMapper postLikeMapper;

	@Override
	public Long save(Long memberId, Long postId) {
		MemberEntity member = memberRepository.getReferenceById(memberId);
		PostEntity post = postRepository.getReferenceById(postId);

		PostLikeEntity savedPostLike = postLikeRepository.save(postLikeMapper.toEntity(member, post));

		return savedPostLike.getId();
	}

	@Transactional
	@Override
	public boolean delete(Long memberId, Long postId) {
		postLikeRepository.deleteByMemberIdAndPostId(memberId, postId);

		return true;
	}
}

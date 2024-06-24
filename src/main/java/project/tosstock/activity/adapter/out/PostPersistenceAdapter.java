package project.tosstock.activity.adapter.out;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.adapter.out.persistence.CommentRepository;
import project.tosstock.activity.adapter.out.persistence.FollowRepository;
import project.tosstock.activity.adapter.out.persistence.PostLikeRepository;
import project.tosstock.activity.adapter.out.persistence.PostRepository;
import project.tosstock.activity.application.domain.model.CustomPage;
import project.tosstock.activity.application.domain.model.MainBoardPostDto;
import project.tosstock.activity.application.domain.model.Post;
import project.tosstock.activity.application.port.out.DeletePostPort;
import project.tosstock.activity.application.port.out.FindPostPort;
import project.tosstock.activity.application.port.out.SavePostPort;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;
import project.tosstock.stock.adpater.out.entity.StockEntity;
import project.tosstock.stock.adpater.out.persistence.StockRepository;

@PersistenceAdapter
@RequiredArgsConstructor
public class PostPersistenceAdapter implements SavePostPort, DeletePostPort, FindPostPort {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final StockRepository stockRepository;

	private final PostLikeRepository postLikeRepository;
	private final CommentRepository commentRepository;
	private final FollowRepository followRepository;

	private final PostMapper postMapper;

	@Override
	public Long save(Post post) {
		MemberEntity proxyMember = memberRepository.getReferenceById(post.getMemberId());
		StockEntity proxyStock = stockRepository.getReferenceById(post.getStockId());

		PostEntity savedPost = postRepository.save(postMapper.toEntity(proxyMember, proxyStock, post));

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

	@Override
	@Transactional(readOnly = true)
	public List<MainBoardPostDto> findPostByArticleContaining(String article, CustomPage page) {
		return postRepository.findMainBoardPostDtoByArticle(article, page.getOffset(), page.getLimit(), page.getSort());
	}

	@Override
	@Transactional(readOnly = true)
	public List<MainBoardPostDto> findPostByStockId(Long stockId, CustomPage page) {
		return postRepository.findMainBoardPostDtoByStockId(stockId, page.getOffset(), page.getLimit(), page.getSort());
	}
}

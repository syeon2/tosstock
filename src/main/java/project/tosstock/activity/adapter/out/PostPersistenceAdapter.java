package project.tosstock.activity.adapter.out;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
import project.tosstock.stock.adpater.out.entity.StockEntity;
import project.tosstock.stock.adpater.out.persistence.StockRepository;

@PersistenceAdapter
@RequiredArgsConstructor
public class PostPersistenceAdapter implements SavePostPort, DeletePostPort, FindPostPort {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final StockRepository stockRepository;

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
	public List<Post> findPostByArticleContaining(String article, Pageable pageable) {
		Page<PostEntity> findPosts = postRepository.findByArticleContaining(article, pageable);

		return findPosts.getContent().stream()
			.map(postMapper::toDomain)
			.collect(Collectors.toList());
	}

	@Override
	public List<Post> findPostByStockId(Long stockId, Pageable pageable) {
		Page<PostEntity> findPosts = postRepository.findByStockId(stockId, pageable);

		return findPosts.getContent().stream()
			.map(postMapper::toDomain)
			.collect(Collectors.toList());
	}
}

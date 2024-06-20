package project.tosstock.activity.application.port.out;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import project.tosstock.activity.application.domain.model.MainBoardPostDto;
import project.tosstock.activity.application.domain.model.Post;

public interface FindPostPort {

	Optional<Post> findPostById(Long postId);

	List<MainBoardPostDto> findPostByArticleContaining(String article, Pageable pageable);

	List<Post> findPostByStockId(Long stockId, Pageable pageable);
}

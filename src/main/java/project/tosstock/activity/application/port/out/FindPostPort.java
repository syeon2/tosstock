package project.tosstock.activity.application.port.out;

import java.util.List;
import java.util.Optional;

import project.tosstock.activity.application.domain.model.CustomPage;
import project.tosstock.activity.application.domain.model.MainBoardPostDto;
import project.tosstock.activity.application.domain.model.Post;

public interface FindPostPort {

	Optional<Post> findPostById(Long postId);

	List<MainBoardPostDto> findPostByArticleContaining(String article, CustomPage page);

	List<MainBoardPostDto> findPostByStockId(Long stockId, CustomPage page);
}

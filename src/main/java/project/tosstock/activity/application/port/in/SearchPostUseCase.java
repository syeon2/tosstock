package project.tosstock.activity.application.port.in;

import java.util.List;

import project.tosstock.activity.application.domain.model.CustomPage;
import project.tosstock.activity.application.domain.model.MainBoardPostDto;

public interface SearchPostUseCase {

	List<MainBoardPostDto> searchPostByArticle(Long memberId, String article, CustomPage page);

	List<MainBoardPostDto> searchPostByStockId(Long memberId, Long stockId, CustomPage pageable);
}

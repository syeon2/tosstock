package project.tosstock.activity.adapter.out.persistence;

import java.util.List;

import project.tosstock.activity.application.domain.model.MainBoardPostDto;

public interface PostRepositoryCustom {

	List<MainBoardPostDto> findMainBoardPostDtoByArticle(Long memberId, String article, Long offset, Long limit,
		String sort);

	List<MainBoardPostDto> findMainBoardPostDtoByStockId(Long memberId, Long stockId, Long offset, Long limit,
		String sort);
}

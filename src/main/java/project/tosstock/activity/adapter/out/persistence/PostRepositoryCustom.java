package project.tosstock.activity.adapter.out.persistence;

import java.util.List;

import project.tosstock.activity.application.domain.model.MainBoardPostDto;

public interface PostRepositoryCustom {

	List<MainBoardPostDto> findMainBoardPostDtoByArticle(String article, Long offset, Long limit, String sort);
}

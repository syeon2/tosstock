package project.tosstock.activity.application.port.in;

import java.util.List;

import org.springframework.data.domain.Pageable;

import project.tosstock.activity.application.domain.model.Post;

public interface SearchPostUseCase {

	List<Post> searchPostByArticle(String article, Pageable pageable);
}

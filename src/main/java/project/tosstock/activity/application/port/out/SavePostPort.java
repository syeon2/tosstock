package project.tosstock.activity.application.port.out;

import project.tosstock.activity.application.domain.model.Post;

public interface SavePostPort {

	Long save(Post post);
}

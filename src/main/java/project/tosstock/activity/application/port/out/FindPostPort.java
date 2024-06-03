package project.tosstock.activity.application.port.out;

import project.tosstock.activity.adapter.out.entity.PostEntity;

public interface FindPostPort {

	PostEntity findPostById(Long postId);
}

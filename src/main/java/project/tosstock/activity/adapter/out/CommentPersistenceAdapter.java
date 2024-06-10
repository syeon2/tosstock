package project.tosstock.activity.adapter.out;

import lombok.RequiredArgsConstructor;
import project.tosstock.activity.adapter.out.entity.CommentEntity;
import project.tosstock.activity.adapter.out.entity.PostEntity;
import project.tosstock.activity.adapter.out.persistence.CommentRepository;
import project.tosstock.activity.adapter.out.persistence.PostRepository;
import project.tosstock.activity.application.domain.model.Comment;
import project.tosstock.activity.application.port.out.DeleteCommentPort;
import project.tosstock.activity.application.port.out.SaveCommentPort;
import project.tosstock.common.annotation.PersistenceAdapter;
import project.tosstock.member.adapter.out.entity.MemberEntity;
import project.tosstock.member.adapter.out.persistence.MemberRepository;

@PersistenceAdapter
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements SaveCommentPort, DeleteCommentPort {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;

	private final CommentMapper commentMapper;

	@Override
	public Long save(Comment comment) {
		MemberEntity member = memberRepository.getReferenceById(comment.getMemberId());
		PostEntity post = postRepository.getReferenceById(comment.getPostId());

		CommentEntity saveComment = commentRepository.save(commentMapper.toEntity(comment, member, post));

		return saveComment.getId();
	}

	@Override
	public Long delete(Long commentId) {
		commentRepository.deleteById(commentId);

		return commentId;
	}
}

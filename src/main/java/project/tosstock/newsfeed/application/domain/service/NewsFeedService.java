package project.tosstock.newsfeed.application.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tosstock.member.application.port.out.FindMemberPort;
import project.tosstock.newsfeed.application.domain.model.TestNewsFeed;
import project.tosstock.newsfeed.application.port.in.NewsFeedFilterUseCase;
import project.tosstock.newsfeed.application.port.out.FindNewsFeedPort;

@Service
@RequiredArgsConstructor
public class NewsFeedService implements NewsFeedFilterUseCase {

	private final FindNewsFeedPort findNewsFeedPort;
	private final FindMemberPort findMemberPort;

	@Override
	@Transactional
	public List<TestNewsFeed> showNewsFeedBasic(Long memberId) {
		return findNewsFeedPort.findNewsFeed(memberId).stream()
			.map(n -> TestNewsFeed.toDomain(n, findMemberPort.findMemberById(n.getMemberId()).get().getUsername()))
			.collect(Collectors.toList());
	}
}

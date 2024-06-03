package project.tosstock.newfeed.application.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.tosstock.member.application.port.out.FindMemberPort;
import project.tosstock.newfeed.application.domain.model.TestNewsFeed;
import project.tosstock.newfeed.application.port.in.NewsFeedFilterUseCase;
import project.tosstock.newfeed.application.port.out.FindNewsFeedPort;

@Service
@RequiredArgsConstructor
public class NewsFeedService implements NewsFeedFilterUseCase {

	private final FindNewsFeedPort findNewsFeedPort;
	private final FindMemberPort findMemberPort;

	@Override
	public List<TestNewsFeed> showNewsFeedBasic(Long memberId) {
		return findNewsFeedPort.findNewsFeed(memberId).stream()
			.map(n -> TestNewsFeed.toDomain(n, findMemberPort.findMemberById(n.getMemberId()).getUsername()))
			.collect(Collectors.toList());
	}
}

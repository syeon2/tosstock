package project.tosstock.newfeed.application.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.tosstock.newfeed.application.domain.model.NewsFeed;
import project.tosstock.newfeed.application.port.in.NewsFeedFilterUseCase;
import project.tosstock.newfeed.application.port.out.FindNewsFeedPort;

@Service
@RequiredArgsConstructor
public class NewsFeedService implements NewsFeedFilterUseCase {

	private final FindNewsFeedPort findNewsFeedPort;

	@Override
	public List<NewsFeed> showNewsFeedBasic(Long memberId) {
		return findNewsFeedPort.findNewsFeed(memberId);
	}
}

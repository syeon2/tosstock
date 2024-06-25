package project.tosstock.activity.application.domain.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomPage {

	private Long offset;
	private Long limit;
	private String sort;

	@Builder
	private CustomPage(Long offset, Long limit, String sort) {
		this.offset = offset;
		this.limit = limit;
		this.sort = sort;
	}

	public static CustomPage of(Long offset, Long limit, String sort) {
		return new CustomPage(offset, limit, sort);
	}
}

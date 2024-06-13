package project.tosstock.stock.application.domain.model;

import lombok.Getter;

@Getter
public enum Market {
	KOSPI("kospi"),
	KOSDOQ("kosdoq");

	Market(String text) {
		this.text = text;
	}

	private final String text;

	public static Market convertStringToEnum(String market) {
		market = market.toLowerCase();
		
		if (KOSPI.text.equals(market)) {
			return KOSPI;
		}

		return KOSDOQ;
	}
}

package project.tosstock.common.wrapper;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ApiResultTest {

	@Test
	@DisplayName(value = "API 응답의 기본 값입니다.")
	void basic_apiResult_of() {
		// given
		String message = "기본 구조입니다.";
		HttpStatus status = HttpStatus.OK;
		Object data = new Object();

		// when
		ApiResult<Object> result = ApiResult.of(status, message, data);

		// then
		assertThat(result.getMessage()).isEqualTo(message);
		assertThat(result.getStatus()).isEqualTo(status.value());
		assertThat(result.getData()).isEqualTo(data);
	}
}

package project.tosstock.common.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.access-token.expiration-minutes}")
	private Integer accessTokenExpiredMinutes;

	@Value("${jwt.refresh-token.expiration-minutes}")
	private Integer refreshTokenExpiredMinutes;

	private static final String PAYLOAD_KEY = "TOSSTOCK";

	public String createToken(String email, TokenType tokenType) {
		return Jwts.builder()
			.signWith(getSignInKey())
			.expiration(getExpiredDate(tokenType))
			.subject(PAYLOAD_KEY)
			.claim("email", email)
			.claim("token_type", tokenType.toString())
			.compact();
	}

	public Claims verifyToken(String token) throws RuntimeException {
		return Jwts.parser()
			.verifyWith(getSignInKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	private SecretKeySpec getSignInKey() {
		return new SecretKeySpec(
			secretKey.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm()
		);
	}

	private Date getExpiredDate(TokenType tokenType) {
		Integer expiredMinutes =
			(tokenType == TokenType.ACCESS_TOKEN) ? accessTokenExpiredMinutes : refreshTokenExpiredMinutes;

		Instant instant = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		return Date.from(instant.plus(expiredMinutes, ChronoUnit.MINUTES));
	}
}

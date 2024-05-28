package project.tosstock.common.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import project.tosstock.common.error.exception.ExpiredTokenException;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.access-token.expired-minutes}")
	private Integer accessTokenExpiredMinutes;

	private static final String PAYLOAD_KEY = "TOSSTOCK";

	public String createToken(String email, TokenType tokenType) {
		JwtBuilder claim = Jwts.builder()
			.signWith(getSignInKey())
			.expiration(getExpiredDate())
			.subject(PAYLOAD_KEY)
			.claim("email", email);

		if (tokenType == TokenType.ACCESS_TOKEN) {
			claim.expiration(getExpiredDate());
		}

		return claim.compact();
	}

	public boolean validateToken(String email, String token) {
		try {
			Claims payload = Jwts.parser()
				.verifyWith(getSignInKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();

			String findEmail = (String)payload.get("email");

			return findEmail.equalsIgnoreCase(email);
		} catch (ExpiredJwtException exception) {
			throw new ExpiredTokenException("만료된 토큰입니다.");
		}
	}

	private SecretKeySpec getSignInKey() {
		return new SecretKeySpec(
			secretKey.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm()
		);
	}

	private Date getExpiredDate() {
		Instant instant = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		return Date.from(instant.plus(accessTokenExpiredMinutes, ChronoUnit.MINUTES));
	}
}

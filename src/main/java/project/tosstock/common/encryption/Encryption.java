package project.tosstock.common.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Encryption {

	public static String createEncryptedSourceBySalt(String source, String salt, EncryptionType type) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(type.getValue());

			byte[] byteData = messageDigest.digest(source.concat(salt).getBytes());

			return IntStream.range(0, byteData.length)
				.mapToObj(i -> String.format("%02x", byteData[i]))
				.collect(Collectors.joining());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("암호화 에러");
		}
	}

	public static String generateSalt() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] salt = new byte[20];

		secureRandom.nextBytes(salt);

		StringBuilder stringBuilder = new StringBuilder();
		for (byte b : salt) {
			stringBuilder.append(String.format("%02x", b));
		}

		return stringBuilder.toString();
	}
}

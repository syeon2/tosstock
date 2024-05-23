package project.tosstock.member.adapter.out.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class TempAuthCodeForMemberRepository {

	private final Map<String, String> store = new HashMap<>();

	public void save(String email, String code) {
		store.put(email, code);
	}

	public Optional<String> findCodeByEmail(String email) {
		return Optional.ofNullable(store.get(email));
	}
}

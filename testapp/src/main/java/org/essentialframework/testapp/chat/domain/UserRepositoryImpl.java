package org.essentialframework.testapp.chat.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.essentialframework.core.annotation.Repository;
import org.essentialframework.testapp.chat.domain.User;

@Repository
public class UserRepositoryImpl implements UserRepository {
	
	Map<String, User> users = new HashMap<>();
	
	@Override
	public List<User> findAll() {
		return new ArrayList<>(users.values());
	}

	@Override
	public User findById(String id) {
		return users.get(id);
	}

	@Override
	public void save(User user) {
		if(user == null) {
			throw new IllegalArgumentException("Cannot save a null user");
		}
		
		if(user.getId() == null) {
			user.setId(UUID.randomUUID().toString());
		}

		users.put(user.getId(), user);
	}

}

package org.essentialframework.testapp.chat.domain;

import java.util.List;

import org.essentialframework.testapp.chat.domain.User;

public interface UserRepository {
	
	List<User> findAll();
	
	User findById(String id);
	
	void save(User user);

}

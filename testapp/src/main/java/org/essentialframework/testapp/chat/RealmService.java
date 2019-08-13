package org.essentialframework.testapp.chat;

import java.util.List;

import org.essentialframework.testapp.chat.domain.Realm;

public interface RealmService {

	List<Realm> findAll();
	
	Realm findById(String id);
	
}

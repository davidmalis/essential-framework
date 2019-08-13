package org.essentialframework.testapp.chat.domain;

import java.util.List;

import org.essentialframework.testapp.chat.domain.Realm;

public interface RealmRepository {

	List<Realm> findAll();
	
	Realm findById(String id);
	
	Realm findByName(String name);
	
}

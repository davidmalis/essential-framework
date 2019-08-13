package org.essentialframework.testapp.chat.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.essentialframework.core.annotation.Repository;
import org.essentialframework.testapp.chat.domain.Realm;

@Repository
public class RealmRepositoryImpl implements RealmRepository {

	Map<String, Realm> realms = new HashMap<>();
	
	public RealmRepositoryImpl() {
		//add default realm
		Realm defaultRealm = new Realm();
		defaultRealm.setId(UUID.randomUUID().toString());
		defaultRealm.setName("Default");
		realms.put(defaultRealm.getId(), defaultRealm);
	}

	@Override
	public List<Realm> findAll() {
		return new ArrayList<>(realms.values());
	}

	@Override
	public Realm findById(String id) {
		return realms.get(id);
	}

	@Override
	public Realm findByName(String name) {
		return realms.values().stream()
		.filter(
			r -> r.getName() != null && r.getName().equalsIgnoreCase(name))
		.findFirst()
		.orElse(null);
	}
	
	
}

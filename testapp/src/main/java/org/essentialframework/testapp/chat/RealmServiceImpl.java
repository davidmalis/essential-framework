package org.essentialframework.testapp.chat;

import java.util.List;

import org.essentialframework.core.annotation.Service;
import org.essentialframework.testapp.chat.domain.Realm;
import org.essentialframework.testapp.chat.domain.RealmRepository;

@Service
public class RealmServiceImpl implements RealmService {

	private RealmRepository realmRepository;
	
	public RealmServiceImpl(RealmRepository realmRespository) {
		this.realmRepository = realmRespository;
	}

	@Override
	public List<Realm> findAll() {
		return realmRepository.findAll();
	}

	@Override
	public Realm findById(String id) {
		return realmRepository.findById(id);
	}
	
}

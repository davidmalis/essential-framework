package org.essentialframework.testapp.chat;

import java.util.List;

import org.essentialframework.core.annotation.Controller;
import org.essentialframework.testapp.chat.domain.Realm;
import org.essentialframework.web.HttpMethod;
import org.essentialframework.web.annotation.PathVariable;
import org.essentialframework.web.annotation.RequestHandler;

@Controller
public class RealmController {
	
	private RealmService realmService;
	
	public RealmController(RealmService realmService) {
		this.realmService = realmService;
	}
	
	@RequestHandler(method = HttpMethod.GET, url = "/realms")
	public List<Realm> getRealms(){
		return realmService.findAll();
	}
	
	@RequestHandler(method = HttpMethod.GET, url="/realms/{id}")
	public Realm getRealmById(@PathVariable("id") String id) {
		return realmService.findById(id);
	}
	
	

}

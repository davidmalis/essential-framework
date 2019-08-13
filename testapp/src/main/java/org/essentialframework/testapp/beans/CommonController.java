package org.essentialframework.testapp.beans;

import java.util.ArrayList;
import java.util.List;

import org.essentialframework.core.annotation.Controller;
import org.essentialframework.web.HttpMethod;
import org.essentialframework.web.annotation.PathVariable;
import org.essentialframework.web.annotation.RequestHandler;

@Controller
public class CommonController {
	
	@RequestHandler(method = HttpMethod.GET, url = "/persons")
	public List<Person> getPersonList() {
		return createPersonList();
	}
	
	@RequestHandler(method = HttpMethod.GET, url = "/persons/{id}")
	public Person getPerson(@PathVariable("id") String personId) {
		return createPersonList().stream().filter(
			p -> Long.valueOf(personId).equals(p.getId())).findFirst().orElse(null);
	}
	
	List<Person> createPersonList(){
		List<Person> users = new ArrayList<>();
		users.add(new Person(1L, "Ivan", "Horvat", 30));
		users.add(new Person(2L, "Petar", "Perić", 25));
		users.add(new Person(3L, "Ivo", "Ivić", 27));
		users.add(new Person(4L, "Ana", "Anić", 25));
		return users;
	}
	

	
}

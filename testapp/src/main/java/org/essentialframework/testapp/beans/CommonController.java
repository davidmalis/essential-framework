package org.essentialframework.testapp.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.essentialframework.core.annotation.Controller;
import org.essentialframework.web.HttpMethod;
import org.essentialframework.web.annotation.RequestHandler;
import org.essentialframework.web.annotation.RequestParameter;

@Controller
public class CommonController{
	
	@RequestHandler(method = HttpMethod.GET, url = "/")
	public List<Person> getPerson( 
			@RequestParameter("name") Optional<String> name,
			@RequestParameter("surname") String surname) {
		return createUserList().stream()
				.filter(u -> {
					
					if(name.isPresent()) {
						return name.get().equalsIgnoreCase(u.getName()) && 
								surname.equalsIgnoreCase(u.getSurname());
					} else {
						return surname.equalsIgnoreCase(u.getSurname());
					}
					
				})
				.collect(Collectors.toList());
	}
	
	List<Person> createUserList(){
		List<Person> users = new ArrayList<>();
		users.add(new Person("Ivan", "Horvat", 30));
		users.add(new Person("Petar", "Perić", 25));
		users.add(new Person("Ivo", "Ivić", 27));
		users.add(new Person("Ana", "Anić", 25));
		return users;
	}
	

	
}

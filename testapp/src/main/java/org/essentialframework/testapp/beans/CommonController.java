package org.essentialframework.testapp.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.essentialframework.core.annotation.Controller;
import org.essentialframework.web.HttpMethod;
import org.essentialframework.web.annotation.RequestHandler;
import org.essentialframework.web.annotation.RequestParameter;

@Controller
public class CommonController{

	
	@RequestHandler(method = HttpMethod.GET, url = "/")
	public List<User> test( @RequestParameter("name") String name ) {
		return createUserList().stream()
				.filter(u -> u.getName().equalsIgnoreCase(name))
				.collect(Collectors.toList());
	}
	
	List<User> createUserList(){
		List<User> users = new ArrayList<>();
		users.add(new User("Ivan"));
		users.add(new User("David"));
		users.add(new User("Mario"));
		users.add(new User("Petar"));
		return users;
	}
	

	
}

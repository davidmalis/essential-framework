package org.essentialframework.testapp.chat;

import java.util.Optional;

import org.essentialframework.core.annotation.Controller;
import org.essentialframework.core.annotation.Wired;
import org.essentialframework.testapp.chat.domain.Login;
import org.essentialframework.testapp.chat.exception.LoginException;
import org.essentialframework.web.HttpMethod;
import org.essentialframework.web.annotation.PathVariable;
import org.essentialframework.web.annotation.RequestHandler;
import org.essentialframework.web.annotation.RequestParameter;

@Controller
public class LoginController {
	
	@Wired
	private LoginService loginService;
	
	@RequestHandler(method = HttpMethod.POST, url = "/login")
	public Login loginByName(
			@RequestParameter("name") String name, 
			@RequestParameter("age") Optional<String> age) 
			throws LoginException {

		return loginService.registerNew(name, age);
	}
	
	@RequestHandler(method = HttpMethod.POST, url = "/login/{id}")
	public Login loginById(@PathVariable("id") String id) throws LoginException {
		return loginService.loginById(id);
	}

}

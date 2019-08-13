package org.essentialframework.testapp.chat;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.essentialframework.core.annotation.Scope;
import org.essentialframework.core.annotation.Service;
import org.essentialframework.core.annotation.Wired;
import org.essentialframework.testapp.chat.domain.Login;
import org.essentialframework.testapp.chat.domain.User;
import org.essentialframework.testapp.chat.domain.UserRepository;
import org.essentialframework.testapp.chat.exception.LoginException;
import org.essentialframework.web.RequestContextHolder;

@Service
@Scope("session")
public class LoginServiceImpl implements LoginService {

	public static final String LOGIN_ATTRIBUTE =
		LoginServiceImpl.class.getName() + ".login";
	
	private HttpSession session = RequestContextHolder.getRequestContext().getSession(true);
	
	@Wired
	private UserRepository userRepository;
	
	@Override
	public Login registerNew(String name, Optional<String> age) throws LoginException {
		
		Login login = new Login();
		User user = new User();
		user.setName(name);
		if(age.isPresent()) {
			user.setAge(Integer.parseInt(age.get()));
		}
		login.setUser(user);
		login.setSessionId(session.getId());

		new LoginValidator().validate(login);
		
		userRepository.save(user);
		session.setAttribute(LOGIN_ATTRIBUTE, login);
		
		return login;
	
	}

	@Override
	public Login loginById(String id) throws LoginException {
		
		User user = userRepository.findById(id);
		
		if(user == null) {
			throw new LoginException("Cannot find user with id="+ id);
		}
		
		Login login = new Login();
		login.setSessionId(session.getId());
		login.setUser(user);
		
		return login;
		
	}
	
	private static class LoginValidator {
		
		private void validate(Login login) throws LoginException {
			//TODO 
		}
		
	}
	
	

}

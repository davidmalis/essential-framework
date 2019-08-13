package org.essentialframework.testapp.chat;

import java.util.Optional;

import org.essentialframework.testapp.chat.domain.Login;
import org.essentialframework.testapp.chat.exception.LoginException;

public interface LoginService {

	Login registerNew(String name, Optional<String> age) 
		throws LoginException;
	
	Login loginById(String id) 
		throws LoginException;
}

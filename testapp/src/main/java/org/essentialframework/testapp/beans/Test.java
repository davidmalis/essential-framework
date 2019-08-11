package org.essentialframework.testapp.beans;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

public interface Test {
	public List<User> test(HttpSession s, Optional<String>p1, Optional<String> p2, Optional<User> u);
}

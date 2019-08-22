package org.essentialframework.testapp.todo.misc;

import java.io.Serializable;

public class ItemListDto implements Serializable {

	private static final long serialVersionUID = 8414977031469119784L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

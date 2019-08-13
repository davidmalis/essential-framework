package org.essentialframework.testapp.chat.domain;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 
		-2016177874112489858L;

	private String id;
	private String name;
	private Integer age;
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getAge() {
		return age;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}

	
	
}

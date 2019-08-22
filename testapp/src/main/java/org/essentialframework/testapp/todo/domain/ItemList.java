package org.essentialframework.testapp.todo.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ItemList implements Serializable {

	private static final long serialVersionUID = -5410522793196636759L;
	
	private String id;
	private String name;
	private LocalDateTime createdAt;
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}

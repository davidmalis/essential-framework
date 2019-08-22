package org.essentialframework.testapp.todo.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Item implements Serializable {

	private static final long serialVersionUID = -1222157078167196679L;
	
	private String id;
	private String itemListId;
	private String name;
	private String description;
	private LocalDateTime createdAt;
	
	private boolean completed;
	private boolean starred;
	
	public String getId() {
		return id;
	}
	public String getItemListId() {
		return itemListId;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setItemListId(String itemListId) {
		this.itemListId = itemListId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public boolean isCompleted() {
		return completed;
	}
	public boolean isStarred() {
		return starred;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public void setStarred(boolean starred) {
		this.starred = starred;
	}

}

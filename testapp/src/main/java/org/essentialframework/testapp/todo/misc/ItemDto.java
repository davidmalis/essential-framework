package org.essentialframework.testapp.todo.misc;

import java.io.Serializable;

public class ItemDto implements Serializable {
	
	private static final long serialVersionUID = 3956392292251320474L;

	private String name;
	private String description;
	private String itemListId;
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String getItemListId() {
		return itemListId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setItemListId(String itemListId) {
		this.itemListId = itemListId;
	}
	
	

}

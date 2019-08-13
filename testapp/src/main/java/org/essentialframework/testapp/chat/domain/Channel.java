package org.essentialframework.testapp.chat.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Channel implements Serializable, Favoritable {

	private static final long serialVersionUID =
		-5517257738137559418L;
	
	private String id;
	private String realmId;
	private String name;
	private List<Message> messages = new ArrayList<>();
	
	public String getId() {
		return id;
	}
	
	public String getRealmId() {
		return realmId;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Message> getMessages() {
		return messages;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setRealmId(String realmId) {
		this.realmId = realmId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

}

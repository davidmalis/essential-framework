package org.essentialframework.testapp.chat.domain;

import java.io.Serializable;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

public class Realm implements Serializable {
	
	private static final long serialVersionUID = 
		709370493244329589L;

	private String id;
	private String name;
	private List<Channel> channels = new ArrayList<>();
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Channel> getChannels() {
		return channels;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

}

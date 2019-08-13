package org.essentialframework.testapp.chat.domain;

import java.io.Serializable;

public class Message implements Serializable, Favoritable {

	private static final long serialVersionUID = 
		8028200400338612646L;
	
	private String id;
	private String channelId;
	private String content;
	
	public String getId() {
		return id;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getContent() {
		return content;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
}

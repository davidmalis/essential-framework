package org.essentialframework.testapp.chat.domain;

import java.io.Serializable;

public class MessageSocialInfo implements Serializable {

	private static final long serialVersionUID = 
		-7567343178276917661L;

	private String messageId;
	private Integer upvotes;
	private Integer downvotes;
	
	public String getMessageId() {
		return messageId;
	}
	
	public Integer getUpvotes() {
		return upvotes;
	}
	
	public Integer getDownvotes() {
		return downvotes;
	}
	
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	public void setUpvotes(Integer upvotes) {
		this.upvotes = upvotes;
	}
	
	public void setDownvotes(Integer downvotes) {
		this.downvotes = downvotes;
	}
	
	
	
}

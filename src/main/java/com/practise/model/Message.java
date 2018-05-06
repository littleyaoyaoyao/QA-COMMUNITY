package com.practise.model;

import java.util.Date;

public class Message {
	private int id;
	private int fromId;
	private int toId;
	private String content;
	private Date createdDate;
	private int hasRead;
	private String conversationId;
	
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFromId() {
		return fromId;
	}
	public void setFromId(int fromId) {
		this.fromId = fromId;
	}
	public int getToId() {
		return toId;
	}
	public void setToId(int toId) {
		this.toId = toId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getHasRead() {
		return hasRead;
	}
	public void setHasRead(int hasRead) {
		this.hasRead = hasRead;
	}

	
	/**
	 * 保证小的id在前面
	 * @return
	 */
	public String getConversationId() {
		return fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId);
	}
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	
	
}

package com.practise.model;

import java.util.Date;
/**
 * 
 * @ClassName:Comment
 * @Description: Comment POJO
 * @author Yao
 * 
 * @date 2018年5月4日 上午11:30:00
 */

public class Comment {
	private int id;
	private int userId;
	private String content;
	private int entityId;
	private int entityType;
	private Date createdDate;
	//状态可以让评论显示/不显示，可操作性变强
	private int status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getEntityId() {
		return entityId;
	}
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}
	public int getEntityType() {
		return entityType;
	}
	public void setEntityType(int entityType) {
		this.entityType = entityType;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}

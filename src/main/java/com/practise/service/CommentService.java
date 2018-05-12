package com.practise.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practise.dao.CommentDao;
import com.practise.model.Comment;

@Service
public class CommentService {

	@Autowired
	CommentDao commentDao;
	
	public List<Comment> getCommentByEntity(int entityId, int entityType){
		return commentDao.selectByEntity(entityId, entityType);
	}
	
	public int addComment(Comment comment){
		return commentDao.addComment(comment);
	}
	
	public int getCommentCount(int entityId, int entityType){
		return commentDao.getCommentCount(entityId, entityType);
	}
	
	public Comment getCommentById(int id){
		return commentDao.getCommentById(id);
	}
	
}

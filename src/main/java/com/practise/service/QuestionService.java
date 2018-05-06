package com.practise.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practise.dao.QuestionDao;
import com.practise.model.Question;


/**
 * Question服务类
 * @ClassName:
 * @Description:
 * @author Yao
 * 
 * @date 2018年5月4日 下午2:39:36
 */
@Service
public class QuestionService {
	@Autowired
	QuestionDao questionDao;
	
	public List<Question> getLatestQuestions(int userId, int offset, int limit) {
		return questionDao.selectLatestQuestions(userId, offset, limit);
    }
	
	public Question getQuestionById(int id){
		return questionDao.getQuestionById(id);
	}
	
	public int addQuestion(Question question){
		return questionDao.addQuestion(question);
	}
	
	public int updateCommentCount(int id, int count){
		return questionDao.updateCommentCount(id, count);
	}
}

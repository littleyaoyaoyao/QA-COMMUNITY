package com.practise;

import java.util.Date;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.practise.dao.QuestionDao;
import com.practise.dao.UserDao;
import com.practise.model.Question;
import com.practise.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Sql("/init-schema.sql")
public class DemoApplicationTests {
	@Autowired
	UserDao UserDao;
	
	@Autowired
	QuestionDao QuestionDao;
	
	@Test
	public void contextLoads() {
		Random random = new Random();
		for(int i = 0; i < 15; i++){
			/*
			User user= new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(500)));
			user.setName(String.format("User%d", i));
			user.setPassword("");
			user.setSalt("");
			UserDao.addUser(user);
			
			user.setPassword("123");
			UserDao.updatePassWord(user);
			*/
			
			Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            question.setCreatedDate(date);
            question.setUserId(i + 1);
            question.setTitle(String.format("headtile{%d}", i));
            question.setContent(String.format("new content txte Content %d", i));
            QuestionDao.addQuestion(question);
			
		}
		/*
        Assert.assertEquals("123", UserDao.selectById(1).getPassword());
        UserDao.deleteById(1);
        Assert.assertNull(UserDao.selectById(1));
        */
	}


	
}

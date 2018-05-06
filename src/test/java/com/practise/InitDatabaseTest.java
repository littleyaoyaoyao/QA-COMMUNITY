package com.practise;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.practise.dao.QuestionDao;
import com.practise.dao.UserDao;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitDatabaseTest {

	@Autowired
	UserDao UserDao;
	
	@Autowired
	QuestionDao QuestionDao;
	
	@Test
	public void contextLoads() {
		System.out.println(QuestionDao.selectLatestQuestions(0, 0, 10));
	}
}

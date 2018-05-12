package com.practise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


//@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,VelocityAutoConfiguration.class })

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		//禁用spring-boot-devtool
		//System.setProperty("spring.devtools.restart.enabled", "false");  
		SpringApplication.run(DemoApplication.class, args);
	}
}

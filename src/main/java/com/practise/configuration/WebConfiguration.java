package com.practise.configuration;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.practise.interceptor.LoginRequestInterceptor;
import com.practise.interceptor.PassInterceptor;

@Component
public class WebConfiguration extends WebMvcConfigurerAdapter{
	
	@Autowired
	PassInterceptor passInterceptor;
	
	@Autowired
	LoginRequestInterceptor loginRequestInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(passInterceptor);
		registry.addInterceptor(loginRequestInterceptor).addPathPatterns("/user/*");
		super.addInterceptors(registry);
	}
	
	 	@Bean  
	    public HttpMessageConverter<String> responseBodyConverter() {  
	        StringHttpMessageConverter converter = new StringHttpMessageConverter(  
	                Charset.forName("UTF-8"));  
	        return converter;  
	    }  
	  
	    @Override  
	    public void configureMessageConverters(  
	            List<HttpMessageConverter<?>> converters) {  
	        super.configureMessageConverters(converters);  
	        converters.add(responseBodyConverter());  
	    }  
	  
	    @Override  
	    public void configureContentNegotiation(  
	            ContentNegotiationConfigurer configurer) {  
	        configurer.favorPathExtension(false);  
	    }  
}

package com.luv2code.springboot.thymeleafdemo.aspect;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DemoLoggingAspect {
	
	//setup logger
	private Logger myLogger = Logger.getLogger(DemoLoggingAspect.class.getName());
	
	@Pointcut("execution(* com.luv2code.springboot.thymeleafdemo.controller.*.*(..))")
	private void forControllerPackage() {}
	
	@Pointcut("execution(* com.luv2code.springboot.thymeleafdemo.service.*.*(..))")
	private void forServicePackage() {}
	
	@Pointcut("execution(* com.luv2code.springboot.thymeleafdemo.dao.*.*(..))")
	private void forDAOPackage() {}
	
	@Pointcut("forControllerPackage() || forServicePackage() || forDAOPackage()")
	private void forAppFlow() {}
	
	@Before("forServicePackage()") 
	public void beforeService() {
		System.out.println("Service AOP layer called...");
	}
	
	@Before("forControllerPackage()") 
	public void beforeController() {
		System.out.println("Controller AOP layer called...");
	}
	
	@Before("forDAOPackage()") 
	public void beforeDAO() {
		System.out.println("DAO AOP layer called...");
	}
	
	@Before("forAppFlow()")
	public void before(JoinPoint theJoinPoint) {
		
		// display the method name called
		
		MethodSignature methodSig = (MethodSignature) theJoinPoint.getSignature();
		System.out.println("Method called :"+methodSig);
		
		
		// display the arguments to the method
		Object[] args = theJoinPoint.getArgs();		
		List<Object> listObj = Arrays.asList(args);
		listObj.forEach(System.out::println);
		
	}
}

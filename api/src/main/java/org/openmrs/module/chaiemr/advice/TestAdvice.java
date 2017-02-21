package org.openmrs.module.chaiemr.advice;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.aop.MethodBeforeAdvice;

public class TestAdvice implements MethodBeforeAdvice{
	
	private static Logger logger = Logger.getLogger(TestAdvice.class);

	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		logger.info("In encounter service advice");
		System.out.println("In encounter service advice");
	}
	
	

}

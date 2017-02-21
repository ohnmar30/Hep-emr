package org.openmrs.module.chaiemr;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.aop.AfterAdvice;

public class AopTest implements AfterAdvice{
	private static Logger logger = Logger.getLogger(AopTest.class);
	 
	private int count = 0;
	 
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
	        logger.info("In test aop");
	    }
}

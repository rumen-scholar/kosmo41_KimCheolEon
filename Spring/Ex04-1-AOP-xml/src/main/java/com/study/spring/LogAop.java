package com.study.spring;

import org.aspectj.lang.ProceedingJoinPoint;

public class LogAop {
	
	public Object loggerAop(ProceedingJoinPoint jointpoint) throws Throwable{
		String signatureStr = jointpoint.getSignature().toShortString();
		System.out.println(signatureStr + " is start.");
		
		long st = System.currentTimeMillis();
		
		try {
			Object obj = jointpoint.proceed();
			return obj;
		} finally {
			long et = System.currentTimeMillis();
			
			System.out.println( signatureStr + " is finished.");
			System.out.println( signatureStr + " 경과시간 : " + (et-st));
		}
	}

}

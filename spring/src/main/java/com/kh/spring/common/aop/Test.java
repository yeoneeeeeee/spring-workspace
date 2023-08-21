package com.kh.spring.common.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component // aop : 런타임시 필요한 위치에 코드를 끼워넣을 수 있도록, bean로 등록시켜줘야함.
@Aspect // 공통관심사(특정 흐름 사이에 끼여서 수행할 코드)가 작성된 클래스임을 명시
		// Aspect어노테이션이 붙은 클래스에는 실행할 코드(advice), pointcut이 정의되어있어야한다.
		// advice(끼여들어서 실제로 수행할 메소드, 코드)
		// @PointCut(advice가 끼여들어서 수행될 클래스 혹은 매서드의 위치등을 의미함.)
public class Test {

	/*
	 * joinpoint : 클라이언트가 호출하는 모든 비즈니스 메서드를 의미하며, advice가 적용될수 있는 예비후보지점들.
	 * 
	 * pointcut  : joinpoint들 중에서 실제로 advice가 실행될 지점을 선택
	 * */
	
	/*
	 * PointCut 작성 방법.
	 * 
	 * @PointCut("execution([접근제한자] 반환형 패키지 + 클래스명.메서드명([매개변수]))")
	 * Pointcut 표현식
	 * 	  * : 모든 | 아무값
	 * 	 .. : 하위 | 아래 (하위패키지) | 0개 이상의 매개변수
	 * */
	
	// @Before: Pointcut에 지정된 메서드가 수행되기 "전"에 advice를 수행하려는 어노테이션
	// com.kh.spring.board 패키지 아래에 있는 클래스들 중 Impl로 끝나는 클래스 내부에 존재하는 모든 메서드(매개변수에 상관없이)에 포인트컷을 지정하겠다.
	
	//@Before("testPointCut()")		//;pointcut의 함수명 testPointCut을 적어넣음
	//@Before("execution(* com.kh.spring.board..*Impl.*(..))")
	public void start() { // 서비스 수행전에 실행되는 메서드(advice)
		log.info("==================== Service start =================");
	}
		
	// @After : Pointcut 에 지정된 메서드가 수행된 "후", advice수행을 하라고 지시하는 어노테이션.
	
	//@After("execution(* com.kh.spring.board..*Impl.*(..))")
	//@After("testPointCut()")		 //;pointcut의 함수명 testPointCut을 적어넣음
	public void end() {
		log.info("==================== Service end =================");
	}
	
	// @Pointcut을 작성해놓을 예정
	// @Pointcut어노테이션에 작성한 패턴을 정의해두는 용도로 사용한다.
	@Pointcut("execution(* com.kh.spring.board..*Impl.*(..))")
	public void testPointCut() {} // 내용 작성 x
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

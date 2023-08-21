package com.kh.spring.security.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kh.spring.security.model.dao.SecurityDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("securityService")
public class SecurityServiceImpl implements SecurityService{

	@Autowired
	private SecurityDao securityDao;
	/*
	 * 
	 * 사용자 인증 프로세스
	 * 
	 * 1) 사용자가 로그인할때 제공한 인증정보를 토대로 사용자가 실제로 존재하는 회원인지 확인
	 * 	  존재한다면 사용자의 인증정보를 반환.
	 * */
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{
		UserDetails loginUser = securityDao.loadUserByUsername(userId);
		log.info("loginUser = {}", loginUser);
		if(loginUser == null) {
			throw new UsernameNotFoundException(userId);
		}
		
		return loginUser;
	}
	
	
	
	
	
}

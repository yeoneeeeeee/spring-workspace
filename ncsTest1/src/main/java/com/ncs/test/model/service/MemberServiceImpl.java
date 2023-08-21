package com.ncs.test.model.service;

import java.lang.reflect.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncs.test.model.dao.MemberDAO;


@Service
public class MemberServiceImpl implements MemberService{

	@Autowired		
	private MemberDAO memberDAO;
	
	@Override 
	public Member loginUser(Member m) {
		
		Member loginUser = memberDAO.loginUser(m); 
		
		return loginUser;
	}
}
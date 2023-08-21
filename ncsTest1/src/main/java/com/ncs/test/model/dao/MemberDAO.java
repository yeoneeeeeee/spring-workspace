package com.ncs.test.model.dao;

import java.lang.reflect.Member;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	public Member loginUser(Member m) {
		return sqlSession.selectOne("memberMapper.loginUser", m);
	}

}
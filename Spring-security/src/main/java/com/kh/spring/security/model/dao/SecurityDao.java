package com.kh.spring.security.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public class SecurityDao {

	@Autowired
	private SqlSessionTemplate sqlSession;

	public UserDetails loadUserByUsername(String userId) {
		return sqlSession.selectOne("security.loadUserByUsername",userId);
	}





















}

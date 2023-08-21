package com.kh.spring.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.vo.Member;


//; MemberServiceImpl : MemberService인터페이스
@Service
public class MemberServiceImpl implements MemberService{
	
	@Autowired		//; MemberDao에 빈객체 등록을 해두었기때문에 member 주입받을수 있음
	private MemberDao memberDao;
	
	@Override //; 있어도 없어도 ok, 어떤 객체인지 명확시해주는 것임
	public Member loginUser(Member m) {
		/*
		 * SqlSessionTemplate 객체를 bean으로 등록한 후에는
		 * 스프링컨테이너가 자원 사용후 자동으로 반납을 해주기 때문에 close()할 필요가 없다. 
		 * */
		Member loginUser = memberDao.loginUser(m); //;없어도 가능
		
		return loginUser;
	}
	
	@Override //; 있어도 없어도 ok, 어떤 객체인지 명확시해주는 것임
	public int insertMember(Member m) {
		return memberDao.insertMember(m);
	}
	
	@Override
	public int idCheck(String userId) {
		return memberDao.idCheck(userId);
	}
	
	@Override
	public int updateMember(Member m) {
		return memberDao.updateMember(m);
	}
	
	@Override
	public Member selectOne(String userId) {
		return memberDao.selectOne(userId);
	}
	
	@Override
	public void updateMemberChangePwd() {
		memberDao.updateMemberChangePwd();
	}
}

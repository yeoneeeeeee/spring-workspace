package com.kh.spring.member.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MemberController {

	@GetMapping("/memberLogin.me")
	public String memberLogin() {
		return "/member/memberLogin";
	}
	
	@GetMapping("/myPage.me")
	public String myPage(Model model,
			// 2. HandlerMapping에 보안인증된 사용자 Authentication 요청하기
			Authentication authentication) {
		
		// 1. SecurityContextHolder로부터 사용자의 인증정보 가져오기
		//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Member principal = (Member) authentication.getPrincipal();
		model.addAttribute("loginUser", principal);
		
		log.info("authentication = {}" , authentication);
		
		return "member/myPage";
	}
	
	@PostMapping("/update.me")
	public String updateMember(
			Member updateMember,
			Authentication oldAuthentication,
			RedirectAttributes ra) {
		log.info("updateMember = {} " , updateMember); //password, authorites x (updateMember에는 해당 정보가 없음)
		// 1. 업무로직 : db의 Member테이브에 회원정보 수정요청을 보낸 회원 정보 수정 => 생략.
		
		// 2. 변경된 회원정보를 DB에서 얻어 온 후 Session에 저장하는 대신.
		// 	  SecurityContext 의 Authentication객체를 직접 수정.
		
		// 누락된 비밀번호 추가.
		updateMember.setUserPwd(((Member)oldAuthentication.getPrincipal()).getPassword());
		
		Collection<? extends GrantedAuthority> oldAuthorites = oldAuthentication.getAuthorities(); // ["ROLE_USER","ROLE_ADMIN",...]
		List<SimpleGrantedAuthority> authorites = new ArrayList();
		for(GrantedAuthority auth : oldAuthorites) {
			SimpleGrantedAuthority simpleAuth = new SimpleGrantedAuthority(auth.getAuthority()); // 문자열형태의 권한을 매개변수로 auth객체 생성
	
			authorites.add(simpleAuth);
		}
		
		updateMember.setAuthorities(authorites);
		
		// 새로운 Authentication 객체 생성
		Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
												updateMember,
												oldAuthentication.getCredentials(),
												oldAuthentication.getAuthorities()
				);
		// SecurityContextHolder에 새로운 인증객체 등록
		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
		
		// 사용자페이지 리다이랙트
		return "redirect:/myPage.me";
	}
}

package com.ncs.test.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ncs.test.model.service.MemberService;
import com.ncs.test.model.vo.Member;


@Controller
@SessionAttributes({"loginUser"})
public class MemberController {
	
	private MemberService mService;
	
	public MemberController() {

	}

	@Autowired 
	public MemberController(MemberService mService) {
		this.mService = mService;
	}
	
	
	@GetMapping("/login")
	public String memberLogin(@ModelAttribute Member m, 
								HttpSession session, 
								Model model) {
		
		Member loginUser = MemberService.loginUser(m);
		String url = "";
		if(loginUser == null) {
			model.addAttribute("errorMsg", "로그인 실패");
			url = "common/errorPage";
		}else {
			session.setAttribute("loginUser", loginUser);
			session.setAttribute("alertMsg", loginUser + "님 환영합니다.");
			url = "redirect:/";
		}
		return "redirect:/";
	}
}

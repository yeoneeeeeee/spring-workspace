package com.kh.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		
		return "main";
	}
	
	@GetMapping("/error/accessDenied.do")
	public String accesDenied() {
		return "common/errorPage";
	}
}

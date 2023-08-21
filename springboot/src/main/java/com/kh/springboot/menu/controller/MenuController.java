package com.kh.springboot.menu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kh.springboot.menu.model.service.MenuService;
import com.kh.springboot.menu.model.vo.Menu;

import lombok.extern.slf4j.Slf4j;


@RestController // @Controller + @ResponseBody 내부의 모든 handler는 @ResponseBody
@Slf4j
public class MenuController {

	@Autowired
	private MenuService menuService;
	
	@GetMapping("/menus")
	public List<Menu> menus(HttpServletResponse response){
		
		List<Menu> list = menuService.selectMenuList();
		log.info("list = {} , " , list);;
		
		// 응답헤더 Access-Controll-Allow-Origin속성추가
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000");
		return list;
	}
	
	@GetMapping("/menus/{type}/{taste}")
	public List<Menu> menus(
			@PathVariable String type , 
			@PathVariable String taste
			){
		log.info("type = {} , taste = {} " , type, taste);
		Map<String, Object> paramMap = new HashMap();
		paramMap.put("type", type);
		paramMap.put("taste", taste);
		
		//; 매개변수 하나만 넘겨줄수 있기때문에 paramMap안에 다 넣어줘서 매개변수로 넣어줌
		List<Menu> list = menuService.selectMenuListByTypeAndTaste(paramMap);
		log.info("list = {} ", list);
		
		return list;
	}
	
	
	/*
	 * @RequestBody 요청시 전달한값중 body에 작성된 "json"문자열을 java의 객체로 변환
	 * */
	//JSON -> VO 로 변환 되는 어노테이션 사용
	
	//@CrossOrigin ? 특정 오리진에 대한 cors정책을 허용할때 사용
	//@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/menu")
	public Map<String, Object> insertMenu( @RequestBody Menu menu){
		log.info("menu = {} ", menu);
		int result = menuService.insertMenu(menu);
		
		Map<String, Object> map = new HashMap<>();
		if(result > 0) {
			map.put("msg", "메뉴 등록 성공");
		}else {
			map.put("msg", "메뉴 등록 실패");
		}
		
		return map;
	}
	
//	@GetMapping("/menu/{id}")
//	public Menu selectOneMenu(@PathVariable String id) {
//		return menuService.selectOneMenu(id);
//	}
	
	// ResponseEntity
	// 존재하지 않는 메뉴번호를 요청한 경우 null값 반환이 아니라 404응답상태를 반환할 예정
	@GetMapping("/menu/{id}")
	public ResponseEntity<Menu> selectOneMenu(@PathVariable String id) {
		Menu menu = menuService.selectOneMenu(id);
		
		if(menu == null) {
			return ResponseEntity.notFound().build();
		}else {
			return ResponseEntity.ok().body(menu); //; ok(): 잘 만들어졌다 오케이, body(): 즉 build라고 보면됨.
		}
	}
	
	//public Map<String, Object> updateMenu(@PathVariable int id, @RequestBody Menu menu){ @PathVariable int id:이미들어있어서 적을 필요 x
	@PutMapping("/menu/{id}")
	public Map<String, Object> updateMenu(@RequestBody Menu menu){
		log.info("menu = {}" , menu );
		int result = menuService.updateMenu(menu);
	
		Map<String,Object> map = new HashMap<>();
		map.put("msg", "메뉴 수정 성공");
		
		return map;
	}
	
	@DeleteMapping("/menu/{id}")
	public ResponseEntity<?> deleteMenu(@PathVariable String id){
		
		log.info("id = {}" , id );
		int result = menuService.deleteMenu(id);
		
		if(result > 0) {
			Map<String, Object> map = new HashMap();
			map.put("msg", "메뉴 삭제 성공");
			return new ResponseEntity( "?" , HttpStatus.OK);
		}else {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}



}

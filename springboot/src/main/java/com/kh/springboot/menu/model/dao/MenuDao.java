package com.kh.springboot.menu.model.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.springboot.menu.model.vo.Menu;

// 저장소 역할하는 빈객체
@Repository
public class MenuDao {

	@Autowired
	private SqlSessionTemplate session;

	public List<Menu> selectMenuList() {
		return session.selectList("menu.selectMenuList");
	}

	//; 매개변수 하나만 넘겨줄수 있기때문에 paramMap안에 다 넣어줘서 매개변수로 넣어줌
	public List<Menu> selectMenuListByTypeAndTaste(Map<String, Object> paramMap) {
		return session.selectList("menu.selectMenuListByTypeAndTaste", paramMap);
	}

	public int insertMenu(Menu menu) {
		return session.insert("menu.insertMenu"         ,        menu);
	}

	public Menu selectOneMenu(String id) {
		return session.selectOne("menu.selectOneMenu" , id);
	}

	public int updateMenu(Menu menu) {
		return session.update("menu.updateMenu" , menu);
	}

	public int deleteMenu(String id) {
		return session.update("menu.deleteMenu" , id);
	}
}

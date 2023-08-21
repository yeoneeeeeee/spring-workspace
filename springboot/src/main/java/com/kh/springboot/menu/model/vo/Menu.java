package com.kh.springboot.menu.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

		private int id;
		private String restaurant;
		private String name;
		private int price;
		private MenuType type; // MenuType(직접만든클래스) db에 저장할때 VARCHAR2
		private String taste;
		
}

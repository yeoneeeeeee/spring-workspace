package com.kh.spring.chat.model.vo;

import lombok.Data;

@Data
public class ChatRoom {

	private int chatRoomNo;
	private String title;
	private String status;
	private int userNo;
	
	private String userName; //;db상없는 데이터
	private int cnt; //;db상없는 데이터 (chatroomjoin과 join 해서 총 사용자 수 알아낼것임)
}

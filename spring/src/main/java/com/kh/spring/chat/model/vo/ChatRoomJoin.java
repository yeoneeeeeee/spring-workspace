package com.kh.spring.chat.model.vo;

import lombok.Data;

@Data
public class ChatRoomJoin {
	//;두개를 통째로 묶어서 하나의 FK로 칠 것이기 때문에 다른 테이블과 중복되지 않음
	private int userNo;
	private int chatRoomNo;
}

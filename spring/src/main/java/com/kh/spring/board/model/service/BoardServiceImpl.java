package com.kh.spring.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.dao.BoardDao;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.BoardExt;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDao boardDao;
	
	public List<Board> selectList(int currentPage, Map<String, Object> paramMap){
		log.info("aop와 둘 중 누가 먼저 실행될까? 나지!");
//	public List<Board> selectList(int currentPage, String boardCode){
		return boardDao.selectList(currentPage, paramMap);
	}
	
   public int selectListCount(Map<String,Object> paramMap) {
// public int selectListCount(String boardCode) {
      return boardDao.selectListCount(paramMap);
   }
   
   
    /*
     * 게시글 + 첨부파일이 함께 삽입 => 이중 하나라도 에러가 발생할 경우 전체 rollback 해줘야함.
     * 
     * @Transactional 선언적 트랜잭션 처리 방법.
     * rollbackFor => rollback처리를 수행하기 위한 에러의 종류.
     * 즉, rollbackFor의 속성값으로 추가된 예외가 발생할 경우 rollback 처리를 시켜준다.
     * 
     * */
    @Transactional(rollbackFor = {Exception.class}) //어떤 종류의 예외가 발생했던 발생했다면 무조건 rollback시키겠다.
	@Override
	public int insertBoard(Board b, List<Attachment> list, String serverFolderPath, String webPath) throws Exception {
    	
    	// 1) 게시글 삽입
    	// 	  게시글 등록 후 해당 게시글의 pk값을 반환 받기.
    	//	  게시글 삽입시 게시글의 제목과, 본문에 들어가는 문자열에 크로스사이트스크립트 공격을 방지하기 위한 메소드 추가.
    	//	  텍스트에리어태그에 엔터, 스페이스바를 개행문자로 변환처리
    	b.setBoardTitle(Utils.XSSHandling(b.getBoardTitle()));
    	b.setBoardContent(Utils.XSSHandling(b.getBoardContent()));
    	b.setBoardContent(Utils.newLineHandling(b.getBoardContent()));
    	
    	int boardNo = boardDao.insertBoard(b); // 반환값은 처리된 행의 갯수가 아닌 pk값을 반환받음
    										   // 제대로 삽입이 안될경우 0을 반환할 예정
    	
    	// 2) 첨부파일 등록 -> list가 비어있지 않은 경우 실행 (버전 1.)
//    	int result = 1;
//    	if(boardNo > 0 && !list.isEmpty()) {
//    		for(Attachment attach   : list) {
//    			attach.setRefBno(boardNo);
//    			attach.setFilePath(webPath);
//    			result = insertAttachment(attach);
//    		}
//    	}
    	
    	// (버전2)
    	int result = 0;
    	if(boardNo > 0 && !list.isEmpty()) {
    		for(Attachment attach : list) {
    			attach.setRefBno(boardNo);
    			attach.setFilePath(webPath);
    		}
    		result = boardDao.insertAttachmentList(list);
    		
    		if(result != list.size()) { // 이미지 삽입 실패시 강제 예외 발생
    			throw new Exception("예외발생");
    		}
    	}
    	
    	return result;
    }
    	
    @Transactional(rollbackFor = {Exception.class}) //; 어떤 예외발생했든 예외시켜라는 뜻
    @Override
    public int updateBoard(Board b, List<MultipartFile> list, String serverFolderPath, String webPath, String deleteList) throws Exception{
    	
    	// 1) XSS, 개행문자 처리
    	b.setBoardTitle(Utils.XSSHandling(b.getBoardTitle()));
    	b.setBoardContent(Utils.XSSHandling(b.getBoardContent()));
    	b.setBoardContent(Utils.newLineHandling(b.getBoardContent()));
    	
    	// 2) 게시글 업데이트 수정
    	int result = boardDao.updateBoard(b);
    	
    	if(result> 0) {
    		
    		// 3) 업로드된 파일들 분류작업.
    		List<Attachment> attachList = new ArrayList();
    		
    		if(list != null) {
    			for(int i=0; i<list.size(); i++) {
    				
    				if(!list.get(i).isEmpty()) {
    					
    					// 변경된 파일명 저장
    					String changeName = Utils.saveFile(list.get(i), serverFolderPath);
    					// Attachment객체를 생성해서 값을 추가한 후 attachList에 추가
    					Attachment at = Attachment
    									.builder()
    									.refBno(b.getBoardNo())
    									.fileLevel(i)
    									.originName(list.get(i).getOriginalFilename())
    									.changeName(changeName)
    									.filePath(webPath)
    									.build();
    					attachList.add(at);
    				}
    			}
    		}
    		
    		// 4) x버튼을 눌렀을때 이미지를 db에서 삭제
    		if(deleteList != null && !deleteList.equals("")) {
    			
    			// 삭제하기 위해서 refBno, deleteList가 필요 //;refBno, deleteList을 map에 담고 dao로 넘겨줄것임
    			Map<String, Object> map = new HashMap();
    			map.put("boardNo", b.getBoardNo());
    			map.put("deleteList", deleteList);
    			
    			result = boardDao.deleteAttachment(map);
    		}
    		
    		// 5) db에서 삭제에 성공했다면 or 게시판 업데이트에 성공했다면
    		if(result > 0) {
    			// Attachment객체 하나하나 업데이트
    			for( Attachment at   :   attachList) {
    				result = boardDao.updateAttachment(at);
    				
    				//result = 0 => 수정작업 실패 => 기존에 첨부파일이 등록 x
    				//result = 1 => 수정작업 성공 => 기존에 첨부파일이 있었으니까 o
    				
    				// 6) 결과값이 0인경우 -> update는 실패했지만, 실제 db에 올라간 첨부파일정보를 등록해야하기 때문에 insert문 실행
    				if(result == 0) {
    					result = boardDao.insertAttachment(at);
    				}
    			}
    		}
    	}

    	return result;
    	
    }
    
    
    
	public int insertAttachment(Attachment attach) {
		return boardDao.insertAttachment(attach);
	}
    	
    @Override
	public BoardExt selectBoard(int boardNo) {
		return boardDao.selectBoard(boardNo);
	}
	
    @Override
	public int increaseCount(int bno) {
		return boardDao.increaseCount(bno);
	}
   
    public int insertReply(Reply r) {
    	return boardDao.insertReply(r);
    }
    
    public List<Reply> selectReplyList(int bno){
    	return boardDao.selectReplyList(bno);
    }
    
    public Attachment selectAttachment(int fileNo) {
    	return boardDao.selectAttachment(fileNo);
    }
    
    public List<String> selectFileList(){
    	return boardDao.selectFileList();
//    	return new BoardDao().selectFileList();
    }
}

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content">
		<br><br>
		<div class="innerOuter">
			<h2>게시글 작성하기</h2>
			<br>
			<form action="${contextPath }/board/insert/${boardCode}" id="enrollForm" enctype="multipart/form-data" method="post">
				<table align="center">
					<tr>
						<th>제목</th>
						<td><input type="text" id="title" class="form-control" name="boardTitle" required></td>
					</tr>
					<tr>
						<th>작성자</th>
						<td>${loginUser.userId }</td>
					</tr>
					<c:if test="${boardCode == 'C' }">
						<tr>
							<th>첨부파일</th>
							<td><input type="file" id="upfile" class="form-control" name="upfile"></td>
						</tr>
					</c:if>
					<c:if test="${boardCode == 'P'}">
					<tr>
						<th><label  for="image">업로드 이미지1</label></th>
						<td>
						<img class="preview" >
						<input type="file" name="upfile" class="form-control inputImage" accept="images/*" id="img1">
						<span class="delete-image">&times;</span>
						</td>
					</tr>
					<tr>
						<th><label  for="image">업로드 이미지2</label></th>
						<td>
						<img class="preview" >
						<input type="file" name="upfile" class="form-control inputImage" accept="images/*" id="img2">
						<span class="delete-image">&times;</span>
					</tr>
					<tr>
						<th><label  for="image">업로드 이미지3</label></th>
						<td>
						<img class="preview">
						<input type="file" name="upfile" class="form-control inputImage" accept="images/*" id="img3">
						<span class="delete-image">&times;</span>
						</td>
					</tr>
					<tr>
						<th><label  for="image">업로드 이미지4</label></th>
						<td>
						<img class="preview" >
						<input type="file" name="upfile" class="form-control inputImage" accept="images/*" id="img4">
						<span class="delete-image">&times;</span>
						</td>
					</tr>
					</c:if>
					<tr>
						<th>내용</th>
						<td>
							<textarea id="content" style="resize:none;" rows="10" class="form-control" 
							name="boardContent" required="required"></textarea>
						</td>
					</tr>
				</table>
				<div align="center">
					<button type="submit" class="btn btn-primary">등록</button>
					<button type="reset" class="btn btn-danger">취소</button>
				</div>				
			</form>
		</div>
	</div>
	<c:if test="${boardCode == 'P' }">
	<script>
		const inputImage = document.getElementsByClassName("inputImage");//null
		
		const preview = document.getElementsByClassName("preview");
		const deleteImage = document.getElementsByClassName("delete-image");
		
		for(let i=0 ; i<inputImage.length ; i++){

		    // 파일이 선택 되었을 때의 동작
		    inputImage[i].addEventListener("change", function(){

		        if(this.files[0] != undefined){ // 파일이 선택 되었을 때
		            const reader = new FileReader(); // 선택된 파일을 읽을 객체 생성
		            reader.readAsDataURL(this.files[0]);
		            // 지정된 파일을 읽음 -> result에 저장(URL 포함) -> URL을 이용해서 이미지 볼 수 있음

		            reader.onload = function(e){ // reader가 파일을 다 읽어온 경우
		                // e.tartget == reader
		                // e.target.result == 읽어들인 이미지의 URL
		                // preview[i] == 파일이 선택된 input태그와 인접한 preview 이미지 태그
		                preview[i].setAttribute("src", e.target.result);

		            }
		      
		        } else { // 파일이 선택되지 않았을 때 (취소)
		            preview[i].removeAttribute("src"); // src 속성 제거
		        }
		    });



		    // 미리보기 삭제 버튼(x)이 클릭 되었을 때의 동작
		    deleteImage[i].addEventListener("click", function(){

		        // 미리보기가 존재하는 경우에만 (이전에 추가된 이미지가 있을 때만 X버튼 동작)
		        if( preview[i].getAttribute("src")  !=  "" ){

		            // 미리보기 삭제
		            preview[i].removeAttribute("src");

		            // input의 값을 "" 만들기
		            inputImage[i].value = "";
		        }

		    });

		}

		
	</script>
	</c:if>
	
	<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>
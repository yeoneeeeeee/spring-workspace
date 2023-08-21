<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="<%=request.getContextPath() %>"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!--  공통적으로사용할 라이브러리 추가 -->
<!-- Jquey 라이브러리 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<!-- 부트스트랩에서 제공하있는 스타일 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<!-- 부투스트랩에서 제공하고있는 스크립트 -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

<script>
	$(()=>{
		$("#loginModal").modal()
						.on('hide.bs.modal' , e => {
						// modal을 비활성화시 이전 페이지로 이동한다.(x버튼, 취소, 모달창 이외의 영역 출력시)
						location.href='${pageContext.request.contextPath}';
					});
	})
</script>
</head>
<body>

  <div class="modal fade" id="loginModal">
      <div class="modal-dialog modal-sm">
         <div class="modal-content">
            <!-- 모달 해더 -->
            <div class="modal-header">
               <h4 class="modal-title">Login</h4>
               <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <form:form action="${contextPath }/login.me" method="post">
               <!--  모달 바디 -->
               <div class="modal-body">
                  <label for="userId" class="mr-sm-2">ID : </label>
                  <input type="text" class="form-controll mb-2 mr-sm-2" placeholder="Enter ID" id="userId" name="userId"> <br>
                  <label for="userPwd" class="mr-sm-2">PWD : </label>
                  <input type="password" class="form-controll mb-2 mr-sm-2" placeholder="Enter Password" id="userPwd" name="userPwd">
               </div>
               
               <!-- 모달 푸터 -->
               <div class="modal-footer">
               		<input type="checkbox" name="remember-me"> 로그인유지
                  <button type="submit" class="btn btn-primary">로그인</button>
                  <button type="button" class="btn btn-danger" data-dismiss="modal">취소</button>
               </div>
            </form:form>
         </div>
      </div>
   </div>

</body>
</html>
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

<!-- alertify -->
<script src="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/alertify.min.js"></script>
<!-- alertify css -->
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/alertify.min.css"/>
<!-- Default theme -->
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/default.min.css"/>
<!-- Semantic UI theme -->
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/semantic.min.css"/>
<style>
div {box-sizing:border-box;}
#header {
   width: 80%;
   height: 130px;
   padding-top:20px;
   margin :auto;
   
   /* 추가 */
   position: sticky;
    top: 0; /* 최상단에 붙임*/
    background-color: white;
    border-bottom : 2px solid black;
    z-index: 10;
}
#header>div{width:100%; margin-bottom: 10px;}
#header_1 {height : 40%;}
#header_2 {height : 60%;}

#header_1>div{
   height:100%;
   float:left;
}
#header_1_left {width :30%; position : relative;}
#header_1_center{width: 40%;}
#header_1_right{width : 30%;}

#header_1_left>img{
height:80%; position:absolute; margin:auto; top:0px; bottom:0px; right:0px; left :0px;
}
#header_1_right {text-align:center; line-height: 35px; font-size: 12px; text-indent:35px;}
#header_1_right>a{margin:5px;}
#header_1_right>a:hover{cursor:pointer}

#header_2>ul{width:100%; height:100%; list-style-type : none; margin:auto; padding: 0; 
    display: flex; /* 추가 */

}
#header_2>ul>li {float:left; width: 25%; height:100%; line-height: 55px; text-align:center;}
#header_2>ul>li a {font-size: 18px; font-weight:900}

#header_2 {border-top : 1px solid lightgray;}

#header a {text-decoration:none; color:black;}

/* 세부페이지에 들어갈 공통 css 부여*/
.content{
   background-color: pink;
   width:80%;
   margin:auto;
}
.innerOuter{
   border:1px solid lightgray;
   width: 80%;
   margin:auto;
   padding: 5% 10%;
   background-color : white;
}
</style>
</head>
<body>

   <c:if test="${ not empty alertMsg }">
      <script>
         alertify.alert("서비스 요청 성공" , '${alertMsg}');
      </script>
      <c:remove var="alertMsg" />
   </c:if>
   <div id="header">
         <div id="header_1">
            <div id="header_1_left">
               <img src="https://www.iei.or.kr/resources/images/main/main_renewal/top_logo.jpg"/>
            </div>
            <div id="header_1_center">
            
            </div>
            <div id="header_1_right">
            
               <!-- 로그인안한경우 -->
               <sec:authorize access="isAnonymous()">
                  <!-- 로그인전 -->
                  <a href="${contextPath }/insert.me">회원가입</a>
                  <!-- 모달의 원리 : 이 버튼 클릭시 data-target에 정의해놓은 해당 아이디의 dom요소를 띄워줌 -->
                  <a href="${contextPath}/memberLogin.me">로그인</a>
               </sec:authorize>
               <sec:authorize access="isAuthenticated()">
                  <!-- 로그인 후 -->
                  <label><sec:authentication property="principal.nickName"/>님 환영합니다.</label> &nbsp;&nbsp;            
                  <a href="${contextPath }/myPage.me">마이페이지</a>
                  <form:form class="d-inline" action="${contextPath}/logout.me" method="post">
                     <a><button class="btn">로그아웃</button></a>
                  </form:form>
               </sec:authorize>
            </div>         
         </div>
         <div id="header_2">
            <ul>
               <li><a href="${contextPath }">HOME</a></li>
               <li><a href="${contextPath }/chat/chatRoomList">채팅</a></li>
               <c:forEach items="${boardTypeList}" var="boardType">
                  <li><a href="${contextPath }/board/list/${boardType.boardCd}">${boardType.boardName }</a></li>            
               </c:forEach>
            </ul>
         </div>
      </div>

   









</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="com.kh.spring.member.model.vo.Member" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
	Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.outer{
		background:black;
		color:white;
		width:1000px;
		margin:auto;
		margin-top:50px;
	}
	
	#enroll-form table {margin:auto;}
	#enroll-form input {margin:5px;}
</style>
</head>
<body>
	
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>

	<div class="outer">
		<br>
		<h2 align="center">내정보 수정</h2>
		
		<form:form id="enroll-form" 
			action="${pageContext.request.contextPath }/update.me" 
			method="post">
			<!-- 회원가입form안에.txt -->
			<table align="center">
				<tr>
					<td>* ID</td>
					<td><input type="text" name="userId" value='<sec:authentication property="principal.userId"/>' readonly>
					</td>
				</tr>
				<%-- <tr>
					<td>* PWD</td>
					<td><input type="password" name="userPwd" value="${loginUser.userPwd }" required></td>
				</tr> --%>
				<tr>
					<td>* Nick NAME</td>
					<td><input type="text" name="nickName" value='<sec:authentication property="principal.nickName"/>'></td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;EMAIL</td>
					<td><input type="email" name="email" value="${loginUser.email }"></td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;BIRTHDAY</td>
					<td><input type="text" name="birthday" value="${loginUser.birthday }" placeholder="생년월일(6자리)"></td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;GENDER</td>
					<td align="center">
					<!-- pageScope로 principal 변수 선언 -->
					<sec:authentication property="principal" var="principal"/>
						<input type="radio" name="gender" value="M" ${principal.gender == 'M'? "checked" : "" }> 남
						<input type="radio" name="gender" value="F" ${principal.gender == 'F'? "checked" : "" }> 여
					</td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;PHONE</td>
					<td><input type="text" name="phone" placeholder="-포함" value="${principal.phone }"></td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;ADDRESS</td>
					<td><input type="text" name="address" value="${principal.address }"></td>
				</tr>
			</table>
			<br>
			<div align="center">
				<button type="reset">초기화</button>
				<button type="submit">수정</button>
			</div>
		</form:form >
	</div>

	<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>
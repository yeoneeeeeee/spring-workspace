<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
</head>
<body>

	<h2>실시간 대기오염 정보</h2>

	지역 :
	<select id="location">
		<option>서울</option>
		<option>부산</option>
		<option>대전</option>
		<option>대구</option>
		<option>경기</option>
	</select>

	<button id="btn1">실시간 대기오염정보 확인</button>
	
	<button id="btn2">실시간 대기오염정보 확인(xml방식)</button>


	<table id="result1" border="1" align="center">
		<thead>
			<tr>
				<th>측정소명</th>
				<th>측정시간</th>
				<th>통합대기환경수치</th>
				<th>미세먼지농도</th>
				<th>일산화탄소농도</th>
				<th>이산화질소농도</th>
				<th>아황산가스농도</th>
				<th>오존농도</th>
			</tr>
		</thead>
		<tbody>

		</tbody>
	</table>


	<script>
		$(()=> {
			
			$("#btn1").click( ()=> {
				$.ajax({
					url : 'air.do',
					data : {"location" : $("#location").val() },
					success : data => {
						console.log(data);
						
						let value = "";
						for(let item of data.response.body.items) { 
							//console.log(item)
							//상단태그에 isELIgnored="true" el무시하겠다 추가해주고 나서 사용가능 
							value += `<tr>
										<td>${item.stationName}</td>
										<td>${item.dataTime}</td>
										<td>${item.khaiValue}</td>
										<td>${item.pm10Value}</td>
										<td>${item.coValue}</td>
										<td>${item.no2Value}</td>
										<td>${item.so2Grade}</td>
										<td>${item.o3Value}</td>
									</tr>`;
						}
						
						$("#result1>tbody").html(value);
					}
				})
			})
			
			
			$("#btn2").click(() => {
				$.ajax({
					url : "air2.do",
					data : {location : $("#location").val()},
					success : data => {
						// WebWorkspace1 , AjaxProject 참고
						// 제이쿼리 메서드 사용가능.
						// find() : 기준이 되는 요소의 하위 요소들을 찾을 때 사용하는 jquery메소드로 xml에서도 적용가능.
						
						// 1. 응답데이터 안에 데이터가 담겨있는 요소선택(item요소)
						console.log($(data).find("item")); //; 유사 배열 만들어진 것 확인가능
						
						// 2. 반복문을 이용해서 데이터가 담긴 요소들에 접근해서 동적으로 요소생성하기.
						let value = "";
						
						$(data).find("item").each((index , item) => {
							
							console.log($(item).find("stationName").text());
							value += "<tr>"
										+"<td>" + $(item).find("stationName").text() + "</td>"
										+"<td>" + $(item).find("dataTime").text() + "</td>"
										+"<td>" + $(item).find("khaiValue").text() + "</td>"
										+"<td>" + $(item).find("pm10Value").text() + "</td>"
										+"<td>" + $(item).find("coValue").text() + "</td>"
										+"<td>" + $(item).find("no2Value").text() + "</td>"
										+"<td>" + $(item).find("so2Grade").text() + "</td>"
										+"<td>" + $(item).find("o3Value").text() + "</td>"
								  +  "</tr>"
						});
						$("#result1>tbody").html(value);
					}
				})
			})
		})
	</script>
</body>
</html>
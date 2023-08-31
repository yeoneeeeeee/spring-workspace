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

	<h2>지진해일 긴급대피장소</h2>

	<!-- Test -->
	<button id="btn">지진해일 긴급대피장소(xml방식)</button>

	코드별 :
	<select id="resultCode">
	</select>
	
	<table id="result1" border="1" align="center">
		<thead>
			<tr>
				<th>결과코드</th>
				<th>결과메시지</th>
				<th>한 페이지 결과 수</th>
				<th>페이지 번호</th>
				<th>전체 결과 수</th>
				<th>수신문서 형식</th>
				<th>시도명</th>
				<th>시군구명</th>
				<th>대피지구명</th>
				<th>대피장소명</th>
				<th>주소</th>
			</tr>
		</thead>
		<tbody>

		</tbody>
	</table>


	<script>
			$("#btn").click(() => {
				$.ajax({
					url : "test.do",
					data : {location : $("#resultCode").val()},
					success : data => {
						
						console.log($(data).find("item")); 
						
						let value = "";
						
						$(data).find("item").each((index , item) => {
							
							value += "<tr>"
										+"<td>" + $(item).find("resultCode").text() + "</td>"
										+"<td>" + $(item).find("resultMsg").text() + "</td>"
										+"<td>" + $(item).find("numOfRows").text() + "</td>"
										+"<td>" + $(item).find("pageNo").text() + "</td>"
										+"<td>" + $(item).find("totalCount").text() + "</td>"
										+"<td>" + $(item).find("type").text() + "</td>"
										+"<td>" + $(item).find("id").text() + "</td>"
										+"<td>" + $(item).find("sido_name").text() + "</td>"
										+"<td>" + $(item).find("sigungu_name").text() + "</td>"
										+"<td>" + $(item).find("remarks").text() + "</td>"
										+"<td>" + $(item).find("shel_nm").text() + "</td>"
										+"<td>" + $(item).find("address").text() + "</td>"
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
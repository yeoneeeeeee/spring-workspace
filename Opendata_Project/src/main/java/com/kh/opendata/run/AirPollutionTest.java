package com.kh.opendata.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kh.opendata.model.vo.AirVo;

public class AirPollutionTest {

	// 한국환경공단_에어코리아_대기오염정보 -> 시도정보
	
	public static final String serviceKey = "h0vO0RO%2B5uEHC5lojdwvA4SEwDLDmrw7LnxfeX7zs4W2vTnTahkpdNgC3SyjvF4eZxa7EXtixpf443tx6zFJgA%3D%3D";

	public String [] locations = {"전국", "서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주", "세종"};
	
	@Test
	public void locationTestRun() throws IOException{
		
		for(String location : locations) {
			testRun(location);
		}
		
	}
	
	// 공공데이터 테스트하기위한 테스트환경 구성
	// 간단한 테스트환경은 src/test/java에 구현하면 된다.
	
	//출력문 : {"response":{"body":{"totalCount":40,"items":[{"so2Grade":"1","coFlag":null,"khaiValue":"72","so2Value":"0.003","coValue":"0.5","pm10Flag":null,"o3Grade":"2","pm10Value":"33","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.025","stationName":"정릉로","pm10Grade":"1","o3Value":"0.057"},{"so2Grade":"1","coFlag":null,"khaiValue":"75","so2Value":"0.003","coValue":"0.5","pm10Flag":null,"o3Grade":"2","pm10Value":"12","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.006","stationName":"도봉구","pm10Grade":"1","o3Value":"0.059"},{"so2Grade":"1","coFlag":null,"khaiValue":"68","so2Value":"0.003","coValue":"0.3","pm10Flag":null,"o3Grade":"2","pm10Value":"19","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.012","stationName":"은평구","pm10Grade":"1","o3Value":"0.051"},{"so2Grade":"1","coFlag":null,"khaiValue":"78","so2Value":"0.004","coValue":"0.4","pm10Flag":null,"o3Grade":"2","pm10Value":"19","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.012","stationName":"서대문구","pm10Grade":"1","o3Value":"0.064"},{"so2Grade":"1","coFlag":null,"khaiValue":"85","so2Value":"0.003","coValue":"0.5","pm10Flag":null,"o3Grade":"2","pm10Value":"25","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.015","stationName":"마포구","pm10Grade":"1","o3Value":"0.072"},{"so2Grade":"1","coFlag":null,"khaiValue":"64","so2Value":"0.003","coValue":"0.5","pm10Flag":null,"o3Grade":"2","pm10Value":"25","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.027","stationName":"신촌로","pm10Grade":"1","o3Value":"0.047"},{"so2Grade":"1","coFlag":null,"khaiValue":"82","so2Value":"0.002","coValue":"0.5","pm10Flag":null,"o3Grade":"2","pm10Value":"23","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.014","stationName":"강서구","pm10Grade":"1","o3Value":"0.068"},{"so2Grade":"1","coFlag":null,"khaiValue":"79","so2Value":"0.003","coValue":"0.5","pm10Flag":null,"o3Grade":"2","pm10Value":"22","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.022","stationName":"공항대로","pm10Grade":"1","o3Value":"0.064"},{"so2Grade":"1","coFlag":null,"khaiValue":"70","so2Value":"0.002","coValue":"0.3","pm10Flag":null,"o3Grade":"2","pm10Value":"16","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.009","stationName":"구로구","pm10Grade":"1","o3Value":"0.054"},{"so2Grade":"1","coFlag":null,"khaiValue":"79","so2Value":"0.003","coValue":"0.2","pm10Flag":null,"o3Grade":"2","pm10Value":"24","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.018","stationName":"영등포구","pm10Grade":"1","o3Value":"0.065"},{"so2Grade":"1","coFlag":null,"khaiValue":"67","so2Value":"0.003","coValue":"0.5","pm10Flag":null,"o3Grade":"2","pm10Value":"26","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.026","stationName":"영등포로","pm10Grade":"1","o3Value":"0.050"},{"so2Grade":"1","coFlag":null,"khaiValue":"85","so2Value":"0.002","coValue":"0.4","pm10Flag":null,"o3Grade":"2","pm10Value":"20","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.011","stationName":"동작구","pm10Grade":"1","o3Value":"0.072"},{"so2Grade":"1","coFlag":null,"khaiValue":"-","so2Value":"0.002","coValue":"0.5","pm10Flag":null,"o3Grade":"2","pm10Value":"21","khaiGrade":null,"sidoName":"서울","no2Flag":null,"no2Grade":null,"o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.030","stationName":"동작대로 중앙차로","pm10Grade":"1","o3Value":"0.062"},{"so2Grade":"1","coFlag":null,"khaiValue":"81","so2Value":"0.003","coValue":"0.5","pm10Flag":null,"o3Grade":"2","pm10Value":"24","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.012","stationName":"관악구","pm10Grade":"1","o3Value":"0.068"},{"so2Grade":"1","coFlag":null,"khaiValue":"73","so2Value":"0.003","coValue":"0.4","pm10Flag":null,"o3Grade":"2","pm10Value":"25","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.016","stationName":"강남구","pm10Grade":"1","o3Value":"0.058"},{"so2Grade":null,"coFlag":"통신장애","khaiValue":"-","so2Value":"-","coValue":"-","pm10Flag":"통신장애","o3Grade":null,"pm10Value":"-","khaiGrade":null,"sidoName":"서울","no2Flag":"통신장애","no2Grade":null,"o3Flag":"통신장애","so2Flag":"통신장애","dataTime":"2023-08-17 12:00","coGrade":null,"no2Value":"-","stationName":"서초구","pm10Grade":"1","o3Value":"-"},{"so2Grade":null,"coFlag":"통신장애","khaiValue":"-","so2Value":"-","coValue":"-","pm10Flag":"통신장애","o3Grade":null,"pm10Value":"-","khaiGrade":null,"sidoName":"서울","no2Flag":"통신장애","no2Grade":null,"o3Flag":"통신장애","so2Flag":"통신장애","dataTime":"2023-08-17 12:00","coGrade":null,"no2Value":"-","stationName":"도산대로","pm10Grade":"1","o3Value":"-"},{"so2Grade":"1","coFlag":null,"khaiValue":"67","so2Value":"0.002","coValue":"0.8","pm10Flag":null,"o3Grade":"2","pm10Value":"18","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"2","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.040","stationName":"강남대로","pm10Grade":"1","o3Value":"0.034"},{"so2Grade":"1","coFlag":null,"khaiValue":"71","so2Value":"0.002","coValue":"0.5","pm10Flag":null,"o3Grade":"2","pm10Value":"28","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.020","stationName":"송파구","pm10Grade":"1","o3Value":"0.055"},{"so2Grade":"1","coFlag":null,"khaiValue":"69","so2Value":"0.002","coValue":"0.4","pm10Flag":null,"o3Grade":"2","pm10Value":"19","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.005","stationName":"강동구","pm10Grade":"1","o3Value":"0.052"},{"so2Grade":"1","coFlag":null,"khaiValue":"64","so2Value":"0.003","coValue":"0.4","pm10Flag":null,"o3Grade":"2","pm10Value":"23","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.024","stationName":"천호대로","pm10Grade":"1","o3Value":"0.047"},{"so2Grade":"1","coFlag":null,"khaiValue":"77","so2Value":"0.003","coValue":"0.3","pm10Flag":null,"o3Grade":"2","pm10Value":"14","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.010","stationName":"금천구","pm10Grade":"1","o3Value":"0.062"},{"so2Grade":"1","coFlag":null,"khaiValue":"89","so2Value":"0.004","coValue":"0.6","pm10Flag":null,"o3Grade":"2","pm10Value":"25","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"2","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.054","stationName":"시흥대로","pm10Grade":"1","o3Value":"0.033"},{"so2Grade":"1","coFlag":null,"khaiValue":"78","so2Value":"0.002","coValue":"0.4","pm10Flag":null,"o3Grade":"2","pm10Value":"18","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.010","stationName":"강북구","pm10Grade":"1","o3Value":"0.063"},{"so2Grade":"1","coFlag":null,"khaiValue":"82","so2Value":"0.003","coValue":"0.5","pm10Flag":null,"o3Grade":"2","pm10Value":"22","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.014","stationName":"양천구","pm10Grade":"1","o3Value":"0.068"},{"so2Grade":"1","coFlag":null,"khaiValue":"74","so2Value":"0.002","coValue":"0.4","pm10Flag":null,"o3Grade":"2","pm10Value":"21","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.007","stationName":"노원구","pm10Grade":"1","o3Value":"0.059"},{"so2Grade":"1","coFlag":null,"khaiValue":"63","so2Value":"0.003","coValue":"0.5","pm10Flag":null,"o3Grade":"2","pm10Value":"25","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"2","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.038","stationName":"화랑로","pm10Grade":"1","o3Value":"0.039"},{"so2Grade":"1","coFlag":null,"khaiValue":"74","so2Value":"0.003","coValue":"0.6","pm10Flag":null,"o3Grade":"2","pm10Value":"21","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.021","stationName":"중구","pm10Grade":"1","o3Value":"0.059"},{"so2Grade":"1","coFlag":null,"khaiValue":"69","so2Value":"0.003","coValue":"0.6","pm10Flag":null,"o3Grade":"2","pm10Value":"44","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.023","stationName":"한강대로","pm10Grade":"2","o3Value":"0.052"},{"so2Grade":"1","coFlag":null,"khaiValue":"79","so2Value":"0.003","coValue":"0.5","pm10Flag":null,"o3Grade":"2","pm10Value":"23","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.016","stationName":"종로구","pm10Grade":"1","o3Value":"0.064"},{"so2Grade":"1","coFlag":null,"khaiValue":"64","so2Value":"0.003","coValue":"0.7","pm10Flag":null,"o3Grade":"2","pm10Value":"30","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.029","stationName":"청계천로","pm10Grade":"1","o3Value":"0.046"},{"so2Grade":"1","coFlag":null,"khaiValue":"67","so2Value":"0.004","coValue":"0.7","pm10Flag":null,"o3Grade":"2","pm10Value":"32","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.029","stationName":"종로","pm10Grade":"1","o3Value":"0.050"},{"so2Grade":"1","coFlag":null,"khaiValue":"71","so2Value":"0.003","coValue":"0.5","pm10Flag":null,"o3Grade":"2","pm10Value":"27","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.026","stationName":"용산구","pm10Grade":"1","o3Value":"0.056"},{"so2Grade":"1","coFlag":null,"khaiValue":"69","so2Value":"0.003","coValue":"0.4","pm10Flag":null,"o3Grade":"2","pm10Value":"25","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.011","stationName":"광진구","pm10Grade":"1","o3Value":"0.053"},{"so2Grade":"1","coFlag":null,"khaiValue":"77","so2Value":"0.003","coValue":"0.4","pm10Flag":null,"o3Grade":"2","pm10Value":"25","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.013","stationName":"성동구","pm10Grade":"1","o3Value":"0.063"},{"so2Grade":"1","coFlag":null,"khaiValue":"72","so2Value":"0.003","coValue":"1.2","pm10Flag":null,"o3Grade":"2","pm10Value":"30","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"2","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.044","stationName":"강변북로","pm10Grade":"1","o3Value":"0.034"},{"so2Grade":"1","coFlag":null,"khaiValue":"66","so2Value":"0.002","coValue":"0.2","pm10Flag":null,"o3Grade":"2","pm10Value":"20","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.009","stationName":"중랑구","pm10Grade":"1","o3Value":"0.049"},{"so2Grade":"1","coFlag":null,"khaiValue":"80","so2Value":"0.003","coValue":"0.4","pm10Flag":null,"o3Grade":"2","pm10Value":"28","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.010","stationName":"동대문구","pm10Grade":"1","o3Value":"0.066"},{"so2Grade":"1","coFlag":null,"khaiValue":"70","so2Value":"0.003","coValue":"0.4","pm10Flag":null,"o3Grade":"2","pm10Value":"6","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.020","stationName":"홍릉로","pm10Grade":"1","o3Value":"0.054"},{"so2Grade":"1","coFlag":null,"khaiValue":"74","so2Value":"0.002","coValue":"0.4","pm10Flag":null,"o3Grade":"2","pm10Value":"25","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2023-08-17 12:00","coGrade":"1","no2Value":"0.014","stationName":"성북구","pm10Grade":"1","o3Value":"0.059"}],"pageNo":1,"numOfRows":50},"header":{"resultMsg":"NORMAL_CODE","resultCode":"00"}}}
	
	// JUnit : 자바 프로그래밍 언어용 유닛 테스트 프레임워크
	//@Test : JUnit이라는 도구를 이용해서 테스트가 가능하도록 설정(테스트하기위해서 굳이 main메소드를 만들필요가 없다)
	//; 위의 locationTestRun메소드 실행시 주석처리
	//@Test
	public void testRun(String location) throws IOException{
		
		// OpenAPI서버로 요청하고자 하는 URL만들었음.
		String url = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
		url += "?serviceKey="+serviceKey;
		url += "&returnType=json";
		url += "&numOfRows=50"; //반환받는 결과값 갯수
		url += "&sidoName="+URLEncoder.encode("서울", "UTF-8");
		
		System.out.println(url);
		
		URL requestUrl = new URL(url); // 요청하고자하는 url주소를 매개변수로 전달하면서 객체생성
		
		// 2. 생성된 url 객체를 가지고 HttpURLConnection 객체 생성.
		HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();
		
		// 3. 전송방식 설정(요청)
		urlConnection.setRequestMethod("GET");
		
		// 4. 요청주소에 적힌 OpenAPI서버로 요청 보낸 후 "스트림"을 활용하여 응답데이터 읽어들이기
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	
		String responseTest ="";
		String line;
		
		while( ( line = br.readLine()) != null ) {
			System.out.println(line);
			responseTest += line;
		}
		
		// SERVICE_KEY_IS_NOT_REGISTERED_ERROR
		
		//System.out.println(responseTest);
		
		// JsonObject, JSONArray, JSONElement를 이용해서 파싱할 수 있음.
		// 즉, responseTest 내에서 원하는 데이터만을 추출할 수 있음.
		
		
		// 각각의 item정보를 airVo객체에 담고 ArrayList에 차곡차곡 쌓기.
		JsonObject totalObj = JsonParser.parseString(responseTest).getAsJsonObject();  //;responseTest문자열형태의 JSON객체임
		
		JsonObject responseObj = totalObj.getAsJsonObject("response");
		// 전체 JSON의 형식으로부터 response속성명에 접근
		//System.out.println(responseObj);
		
		JsonObject bodyObj = responseObj.getAsJsonObject("body");
		System.out.println(bodyObj);
		
		int totalCount = bodyObj.get("totalCount").getAsInt();
		//System.out.println(totalCount);
		
		
		// JsonObject itemsObject = bodyObj.getAsJsonObject("items");
		JsonArray itemsArr = bodyObj.getAsJsonArray("items");
		//System.out.println(itemsArr);
		
		ArrayList<AirVo> list = new ArrayList();
		
		for(int i = 0; i<itemsArr.size(); i++) {
			
			JsonObject item = itemsArr.get(i).getAsJsonObject();
			//System.out.println(item);
			
			AirVo air = new AirVo();
			
			air.setStationName( item.get("stationName").getAsString() );
			
			if(item.get("dataTime") != null) {
				air.setDataTime(item.get("dataTime").getAsString());
			}
			
			air.setKhaiValue(item.get("khaiValue").getAsString());
			air.setPm10Value(item.get("pm10Value").getAsString());
			air.setSo2Value(item.get("so2Value").getAsString());
			air.setCoValue(item.get("coValue").getAsString());
			air.setNo2Value(item.get("no2Value").getAsString());
			air.setO3Value(item.get("o3Value").getAsString());

			
			list.add(air);
		}
		
		// list 내부에 있는 vo 객체 확인
		for (AirVo a    :      list) {
			System.out.println(a);
		}
		
		br.close();
		urlConnection.disconnect();
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
}

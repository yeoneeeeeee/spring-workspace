package com.kh.spring.member.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.validator.MemberValidator;
import com.kh.spring.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;
@Slf4j

//; @Controller: servlet-context.xml이 자동으로 빈 클래스로 등록을 시켜준다
@Controller
// Controller 타입의 어노테이션을 붙여주면 빈 스캐너가 자동으로 빈(bean)으로 등록해줌.
// (servlet-context.xml안에 있는 <context:component-scan>태그)
// @RequestMapping("/member") -> 공통주소(클래스레벨에 선언)
// localhost:8081/spring/member(공통주소)/login.me(그외주소, 메소드레벨에 선언)
// 단, 클래스레벨에 @RequestMapping이 존재하지 않는 경우 메서드레벨에서 단독으로 요청을 처리한다.

@SessionAttributes({"loginUser", "nextUrl"})
// Model에 추가된 값의 key와 일치하는 값이 있으면 해당값을 session scope로 이동시킨다.
public class MemberController extends QuartzJobBean{

	/*
	 * 기존객체 생성 방식 private MemberService mService = new MemberService();
	 * 
	 * 서비스가 동시에 많은 횟수가 요청이되면 그만큼 많은 객체가 생성된다. //; 메모리 관리상 유연성이 없다.
	 * 
	 * Spring의 DI(Dependency Injection) -> 객체를 스프링에서 직접 생성해서 주입해주는 개념
	 * 
	 * new 연산자를 쓰지 않고 선언만 한 후 @Autowired어노테이션을 붙이면 객체를 주입받을수 있다.
	 */

	//@Autowired //; spring 형식의 MemberService객체를 주입받고 있는 방식
	private MemberService mService;
	private MemberValidator memValidator;
	private BCryptPasswordEncoder bcryptPasswordEncoder; 
	/*
	 * ; 객체를 주입받는 방식이 여러가지 있다.
	 * 
	 * 필드주입방식 장점 : 이해하기 편함. 사용하기도 편함.
	 * 
	 * 필드주입방식의 단점 : 순환 의존성 문제가 발생할 수 있다. 무분별한 주입시 의존관계 확인이 어렵다. final 예약어를 지정할수가 없다.
	 * //; 즉 객체가 변경될 수 있으므로 위험하다.
	 * 
	 */

	// 생성자 주입방식

	public MemberController() {

	}

	@Autowired // ; 여기서 붙여주면 위에 @Autowired는 지워주기
	public MemberController(MemberService mService, MemberValidator memValidator, BCryptPasswordEncoder bcryptPasswordEncoder) {	//;MemberValidator memValidator : memValidator객체를 주입해줄것임.
		this.mService = mService;
		this.memValidator = memValidator;
		this.bcryptPasswordEncoder = bcryptPasswordEncoder;
	}
	/*
	 * 의존성 주입시 권장하는 방식 (;객체를 주입하는 것임) 생성자에 참조할 클래스로 인자를 받아서 필드에 매핑시킴
	 * 
	 * 장점 : 현재클래스에서 내가 주입시킬 객체들을 모아서 관리할 수 있기 때문에 한눈에 알아보기 편함 코드분석과 테스트에 유리하며,
	 * 		 final로 필드값을 받을수 있어서 안전하다.
	 */

	/*
	 * 그외 방식 Setter 주입방식 : setter메서드로 빈을 주입받는 방식 생성자에 너무 많은 의존성을 주입하게 되면 알아보기 힘들다는
	 * 단점이 있어서 보완하기 위해 사용하거나, 혹은 의존성이 항상 필요한 경우가 아니라 선택사항이라면 사용함.
	 */

	@Autowired // ; 쓰기 좀 어려워서 있다는 것만 알아두기
	public void setMemberService(MemberService memeService) {
		this.mService = memeService;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(memValidator);	//; id,name,pwd 에는 빈값이 들어가면 안되게 만들어줌.
	}

//	-------------------------------필드----------------------------- //

//	; method 자체가 없으면 get이든 post이든 신경쓰지 않겠다.

//	@RequestMapping(value = "login.me" , method = RequestMethod.POST)
//	RequestMapping이라는 어노테이션을 붙이면 HandlerMapping곳에 등록이 됨 //; HandlerMapping을 관리하는 것이 DispatchServlet임
//	()안에 여러개의 속성을 추가 할수 있음.

	/*
	 * 1) 스프링에서 parameter(요청시 전달값)을 받는 방법
	 * 1. HttpServletRequest request를 이용해서 전달받기(기존방식 그대로)
	 * 해당 메소드의 매개변수로 HttpServletRequest를 작성해 놓으면 스프링 컨테이너가 해당 메소드를 호출할때
	 * 자동으로 request객체를 생성해서 매개변수로 주입해준다.
	 * */
	
//	public String loginMember(HttpServletRequest request) {
//		String userId = request.getParameter("userId");
//		String userPwd = request.getParameter("userPwd");
//		
//		System.out.println("userId : "+userId );
//		System.out.println("userPwd : "+userPwd);
//		
//		return "main";
//	}

	/*
	 * 2. @RequestParam어노테이션을 이용하는 방법 
	 * 기존의 request.getParameter("키")로 뽑는 역할을 대신 수행해주는
	 * 어노테이션 input 속성의 value로 jsp에서 작성했던 name값을 입력해주면 알아서 매개변수로 값을 담아온다. 
	 * 만약 넘어온값이 비어있다면 defaultValue로 설정 가능.
	 */
//	@RequestMapping(value = "login.me" , method = RequestMethod.POST)
//	public String loginMember(
//					//@RequestParam(value="userId") String userId,
//					@RequestParam(value="userId" , defaultValue="m") String userId, //;defaultValue="m" 값이 없을 때 디폴트값, 꼭 입력하지 않아도 되는 경우 사용.
//					@RequestParam(value="userPwd") String userPwd
//			) {
//		System.out.println("userId : "+userId );
//		System.out.println("userPwd : "+userPwd);
//		
//		return "main";
//	}

	/*
	 * 3. @RequestParam어노테이션을 생략하는 방법 
	 * 단, 매개변수의 변수명을 jsp에서 전달한 파라미터의 name속성값과 일치시켜줘야한다. + defaultValue 사용불가
	 */
//	@RequestMapping(value = "login.me" , method = RequestMethod.POST)
//	public String loginMember(
//					String userId,
//					String userPwd
//			) {
//		System.out.println("userId : "+userId );
//		System.out.println("userPwd : "+userPwd);
//		
//		return "main";
//	}

	/*
	 * 4. 커멘드 객체 방식 해당 메소드의 매개변수로 요청시 전달값을 담고자하는 VO클래스타입의 변수를 셋팅하고, 요청시 전달값의
	 * name속성값이 VO클래스의 담고자하는 필드명과 일치시켜서 작성
	 * 
	 * 스프링컨테이너에서 해당 객체를 "기본 생성자"로 호출해서 생성 후, 내부적으로 전달받은 key값에 해당하는 setter메서드를 찾아서
	 * 전달한값을 필드에 담아준다. 따라서 반드시 name속성값(키값)과 vo객체의 필드명이 일치해야한다.
	 */
//	@RequestMapping(value = "login.me" , method = RequestMethod.POST)
//	public String loginMember(
//			@ModelAttribute Member m 
//			//; @ModelAttribute : 생략 가능
//			) {
//		System.out.println("userId : "+m.getUserId() );
//		System.out.println("userPwd : "+m.getUserPwd());
//		
//		return "main";
//	}

	
//	  @RequestMapping(value = "login.me" , method = RequestMethod.POST) 
//	  public ModelAndView loginMember( //; ModelAndView 쓸때는 반환형 ModelAndView로 변경해주기
//	  
//	  @ModelAttribute Member m , HttpSession session, Model model, ModelAndView mv ) {
	  //; 로그인하기 위해 로그인 정보를 담은 session객체
	  
	 /* 요청 처리 후 "응답 데이터를 담고" 응답페이지로 url재요청 하는 방법 
	  1) Model 객체 이용
		포워딩할 응답뷰로 전달하고자 하는 데이터를 맵형식으로 담을 수 있는 객체 (Model객체는 requestScope를 가지고 있음) 
	  -> request, session을 대신하는 객체
	  
	  - 기본 scope : request이고, session scope로 변환하고 싶은 경우 클래스 위에 @SessionAttribute를
	  작성하면 된다. model 안에 데이터를 추가하는 함수 : addAttribute()
	  
	  2) ModelAndView 객체 이용 //; session scope는 안담겨서 session scope를 이용하고 싶으면 Model객체를 사용해야한다. 
	  ModelAndView에서 Model은 데이터를 담을수 있는 key=value형태의 객체(위 Model과 동일) 
	  View는 이동하고자하는 페이지에 대한 정보를 담고있는 객체. 합쳐서 ModelAndView mv에 model에 데이터를 추가하는 함수
	  mv.addObject(key,value) mv에 view에 데이터를 추가하는 함수 (; mv = ModelAndView)
	  mv.setViewName("이동할페이지")
	  
	  -> Model로 데이터를 전달하든, ModelAndView로 데이터를 전달하든 
	     결국은 ModelAndView로 통합되서 Spring container에 의해 관리된다.*/
	  
//	  model.addAttribute("errorMsg","오류발생"); //; 기본값으로 request를 갖고 있다.
//	  
//	  mv.addObject("errorMsg", "modelAndView 테스트");
//	  mv.setViewName("common/errorPage");
//	  
//	  System.out.println("userId : "+m.getUserId() );
//	  System.out.println("userPwd : "+m.getUserPwd());
//	  
//	  return mv; 
//	  
//	  }
	 

	@PostMapping("login.me")
	public String loginMember(
							@ModelAttribute Member m, 
							HttpSession session,
							Model model,
							@SessionAttribute(required = false)String nextUrl
			) {
		log.info("확인할래요 {}, {}", m, m); //;콘솔창 확인가능
		//log.info("확인할래요 {}", m, m); 찍어볼게 여러개면 뒤에 추가
		
		// 암호화 전 로그인 요청처리
//		Member loginUser = mService.loginUser(m);
//		String url = "";
//		if(loginUser == null) {
//			model.addAttribute("errorMsg", "오류발생"); // ; 기본값으로 request를 갖고 있다.
//			url = "common/errorPage";
//		}else {
//			session.setAttribute("loginUser", loginUser);
//			url = "redirect:/";
//		}
		
		// 암호화 후 로그인 요청처리
		Member loginUser = mService.selectOne(m.getUserId());
		// loginUser 안의 userPwd는 암호화된 상태의 비밀번호가 들어있음.
		// m 안의 userPwd는 암호화전 상태의 비밀번호
		
		// BcrytPasswordEncoder객체의 matches 메소드 사용
		// matches(평문, 암호문)을 작성하면 내부적으로 두 값이 일치하는 검사 후 일치하면 true/ 일치하지 않으며 false
		
		String url = "";
		if(loginUser != null && bcryptPasswordEncoder.matches(m.getUserPwd(), loginUser.getUserPwd())) {
			// 로그인 성공
			if(loginUser.getChangePwd().equals("Y")) {
				session.setAttribute("alertMsg","비밀번호를 변경해주세요.");
			}
			model.addAttribute("loginUser", loginUser);	//; model은 특정 어노테이션에 적용하면 값을 session에 담을 수 있다.
			
			url = "redirect:" + (nextUrl != null ? nextUrl : "/"); //; null이아니면 nextUrl주소, null이면 기본경로 이동
			
			// 사용한 nextUrl제거
			model.addAttribute("nextUrl", null);
		} else {
			model.addAttribute("errorMsg", "아이디 또는 비밀번호가 일치하지 않습니다.");
			url = "common/errorPage";
		}
		
		
		return url;
	}
	
	@GetMapping("/logout.me")
	public String logoutMember(HttpSession session , SessionStatus status ) {	// @SessionAttributes Member loginUser 처럼 각 함수에 있는 매개변수를 꺼내서 쓸 수 있다.
		
		session.invalidate();
		
		// @SessionAttributes로 session scope에 이관된 데이터는 sessionstatus를 이용해서 객체를 없애야한다.
		session.invalidate();
		status.setComplete();
		
		return "redirect:/";
	}
	
	@GetMapping("/insert.me") //;a태그 url요청은 @GetMapping
	public String enrollForm() {
		return "member/memberEnrollForm";
	}
	
	@PostMapping("/insert.me")
	public String insertMember(/* @ModelAttribute */@Validated Member m, HttpSession session, Model model, BindingResult bindingResult) { //;session 회원가입, model에는 에러메세지
//		if(bindingResult.hasErrors()) {	//;값을 못넣어주고 있는 코드.
//			String errors = "";
//			List<ObjectError> errorsList = bindingResult.getAllErrors();
//			for(ObjectError err : errorsList) {
//				errors += "{"+err.getCode()+":"+err.getDefaultMessage() +"} ";
//				model.addAttribute("errorMsg", errors);
//			}
//			
//			return "common/errorPage";
//		}
		// 멤버테이블에 회원가입등록 성공시 -> alertMsg변수에 회원가입 성공 메세지 담아서 main 페이지로 url재요청 보내기
		//					  실패시 ->
		
		
		
		// 암호화 작업전 회원가입 프로세스
		
		// 1. memberService호출해서 insertMember 실행(Insert)
//		int result = mService.insertMember(m);
//		String url = "";
//		
//		if(result > 0) {
//			//성공시 
//			session.setAttribute("alertMsg", "회원가입 성공");
//			url = "redirect:/";
//		}else {
//			//실패 -에러페이지로
//			model.addAttribute("errorMsg", "회원가입 실패");
//			url = "common/errorPage";
//		}
		
		
		
		
		// 암호화 작업후 회원가입 프로세스 
		
		
		/*
		 * 비밀번호가 사용자가 입력한 그대로이기때문에 보안에 문제가 있다.
		 * -> BCrypt방식의 암호화를 통해서 pwd를 암호문으로 변경.
		 * 1) spring security모듈에서 제공하는 라이브러리를 pom.xml다운
		 * 2) BCryptPasswordEncoder클래스를 xml파일에서 bean객체로 등록
		 * 3) web.xml에 2번에서 생성한 xml파일을 로딩할 수 있도록 param-value에 추가.
		 * */
		System.out.println("암호화 전 비밀번호 : "+m.getUserPwd());
		
		// 암호화 작업 (;단방향 작업의 암호화임 - 비번까먹으면 못찾음)
		String encPwd = bcryptPasswordEncoder.encode(m.getUserPwd());
		
		// 암호화된 pwd를 Member m에 담아주기 
		m.setUserPwd(encPwd);
		
		System.out.println("암호화 후 비밀번호 : "+m.getUserPwd());
		
		// 1. memberService호출해서 insertMember 실행(Insert)
		int result = mService.insertMember(m);
		String url = "";
		
		if(result > 0) {
			//성공시 
			session.setAttribute("alertMsg", "회원가입 성공");
			url = "redirect:/";
		}else {
			//실패 -에러페이지로
			model.addAttribute("errorMsg", "회원가입 실패");
			url = "common/errorPage";
		}
		
		return url;
	}
	
	
	@GetMapping("/myPage.me")
	public String myPage() {
		return "member/myPage";
	}
	
	
	// 내정보수정 성공시 -> myPage로 url재요청
	// 실패시 -> 에러페이지로
	@PostMapping("/update.me")
	public String updateMember(Member m, HttpSession session, Model model, RedirectAttributes ra) {
		
		int result = mService.updateMember(m);
		
		if(result > 0) {
			// 업데이트 성공시 db에 등록된 회원정보 다시 불러오기 
			Member updateMember = mService.loginUser(m);
			session.setAttribute("loginUser", updateMember);
			ra.addFlashAttribute("alertMsg", "내정보수정 성공"); //; 입력한 정보를 그대로 유지한상태로 정보수정시 사용
			// 1차적으로 alertMsg sessionScope로 이관
			// 리다이렉트 완료 후 sessionScope에 저장된 alertMsg를 requestScope로 다시 이관 //; 자주 사용보단 1회성시에만 사용 권장
			
			//session.setAttribute("alertMsg", "내정보수정 성공");
			
			//return "member/myPage"; //  forward -> 그 값을 그대로 유지한채로 해당페이지 요청 //; 이후 더 작업처리할 것이 있으면 포워드
			
			return "redirect:/myPage"; //  redirect -> 새로고침을 실행 url요청 //; 이후 더 작업처리할것이 없다면 리다이렉트
		}else {
			//실패 -에러페이지로
			model.addAttribute("errorMsg", "내정보수정 실패");
		}return "member/myPage";
		
	}
	
	/*
	 * 스프링 예외처리 방법(3가지, 중복으로 사용 가능!)
	 * 
	 * 1. 메서드별로 예외처리(try-catch / throws) -> 1순위로 적용됨 
	 * 
	 * 2. 하나의 컨트롤러에서 발생하는 예외를 모아서 처리하는 방법 => @ExceptionHandler(메서드에 작성) -> 2순위
	 * 
	 * 3. 전역에서 발생하는 예외를 모아서 처리하는 클래스 -> @ControllerAdvice(클래스에 작성) -> 3순위
	 * 
	 * */
	
//	@ExceptionHandler(Exception.class)
//	public String exceptionHandler(Exception e, Model model) {
//		e.printStackTrace();
//		
//		model.addAttribute("errorMsg", "서비스 이용중 문제가 발생했습니다.");
//		
//		return "common/errorPage";
//	}
	
	//아이디 중복검사 --> 비동기요청
	@ResponseBody //비동기 요청시 사용. //;값이 넘어갈때 꼭 사용
	@GetMapping("/idCheck.me")
	public String idCheck(String userId) {
		
		int result = mService.idCheck(userId);
		
		/*
		 * 컨트롤러에서 반환되는 값은 forward 또는 redirect를 위한 경로인 경우가 일반적임. 즉, 반환되는 값은
		 * 경로로써 인식함.
		 * 
		 * 이를 해결하기 위한 어노테이션이 @ResponseBody
		 * 	-> 반환되는 값을 응답(response)의 몸통(body)에 추가하여 이전 요청주소로 돌아감
		 * 	=> 컨트롤러에서 반환되는 값이 경로가 아닌 "값 자체"로 인식됨.
		 * 
		 * 
		 * */
		
		return result +"";
	}

	/*
	 * Spring 방식 ajax요청 처리 방법  //; 3가지 방법
	 * jsonView빈을 통해 데이터를 처리하기
	 * */
	@PostMapping("/selectOne")
	public String selectOne(String userId, Model model) {
		// 1. 업무로직
		Member m = mService.selectOne(userId);
		
		if(m != null) {
			model.addAttribute("userId", m.getUserId()); //; model에 m을 넣어주기
			model.addAttribute("userName", m.getUserName()); //; id와 userName 객체 안에 키값으로 userId, userName을 집어 넣어준다.
		}
		
		// model 객체 안에 담긴 데이터를 json으로 변환후 응답처리해줌.
		// 내가 요청한 jsonView가 jsp파일이 아닌 실제 bean이름으로 매핑을 시켜주는 BeanNameViewResolver를 추가해줘야함.
		return "jsonView"; //; 실무상에서는 jsonView를 가장 많이 씀
	}
	
	//;자바의 객체형태의 데이터를 자동으로 JSON형태로 변환해줄 Bean객체 servlet-context.xml , pom.xml에서 의존성 추가해준다.
	@ResponseBody //비동기 요청시 사용. //; 값자체를 내보내는 역할을 한다. 하지만 문자열형태만 내보낼수있고 Map인 문자열형태가 아닌 기본 자바형객체는 빈객체로 인식하여 내보낼수없다. 
	@PostMapping("/selectOne2")
	public Map<String, Object> selectOne2(String userId) {  //;Map이던 Member이던 상관없다.
		// 1. 업무로직
		Member m = mService.selectOne(userId);
		Map<String, Object> map = new HashMap();
		
		if(m != null) {
			map.put("userId", m.getUserId());
			map.put("userName", m.getUserName());
		}
		return map;
	}
	
	@PostMapping("/selectOne3")  //; ResponseEntity가 제일 많이 쓰임
	public ResponseEntity<Map<String, Object>> selectOne3(String userId) {  //;Map이던 Member이던 상관없다.
		// 1. 업무로직
		Member m = mService.selectOne(userId);
		Map<String, Object> map = new HashMap();
		
		ResponseEntity res = null;
		
		if(m != null) {
			map.put("userId", m.getUserId());
			map.put("userName", m.getUserName());
			
			res = ResponseEntity
					 .ok() //;ok():응답상태를 작성할수있다. ex)status = 200/400/404/500 등 에러페이지 조정한 내용
					 .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)  //; html형태 파일을 json형태로 자유롭게 내보낼수있다.
					 .body(map);
		}else {
			res = ResponseEntity
					.notFound()
					.build();
		}
		
//		ResponseEntity res = ResponseEntity
//							 .ok() //;ok():응답상태를 작성할수있다. ex)status = 200/400/404/500 등 에러페이지 조정한 내용
//							 .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)  //; html형태 파일을 json형태로 자유롭게 내보낼수있다.
//							 .body(map);
		return res;
	}
	
	
	
	
	//@Scheduled(fixedDelay = 1000)//고정방식 //;서버가 켜지자마자 1m/s로 실행됨
	public void test() {
		log.info("1초마다 출력");
	}

	public void testCron() {
		log.info("크론탭 방식 테스트 ");
	}
	
	public void testQuartz() {
		log.info("zhkcm 테스트 ");
	}
	
	/*
	 * 회원정보 확인 스케줄러
	 * 매일 오전 1시에 모든 사용자의 정보를 검색하여 사용자가 비밀번호를 안바꾼지 3개월이 지났다면, changePwd 이 칼럼에 값을
	 * Y로 변경.
	 * 
	 * 사용자가 로그인했을때 changePwd값이 Y
	 * 
	 * */
	
	@Override
	public void executeInternal(JobExecutionContext context) throws JobExecutionException{
		// JobDataAsMap으로 등록한 bean 객체 가져옴
		MemberService mService = (MemberService) context.getMergedJobDataMap().get("mService"); 
		
		mService.updateMemberChangePwd();
		
	}
}

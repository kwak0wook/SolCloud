package com.sol4.cloud.Controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.sol4.cloud.Pagination;
import com.sol4.cloud.RSA;
import com.sol4.cloud.SessionAbandon;
import com.sol4.cloud.Domain.Cloud;
import com.sol4.cloud.Service.MemberService;

@Controller
@SessionAttributes("sessionID")
public class MemberController {
	
	@Autowired
	private RSA rsa;
	
	@Autowired
	private MemberService mService;
	
	private static final SessionAbandon SA = new SessionAbandon();

	@RequestMapping(value="/", method = RequestMethod.GET)
	public String home (HttpSession session) {
		if(session.getAttribute("sessionID") != null) {
			return "redirect:boardMain.go";
		}
		return "redirect:login.go";
	}
	
	@RequestMapping(value="login.do", method = RequestMethod.POST)
	public ModelAndView login_do (HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Cloud m = rsa.decryptRsaDo(request);
		ModelAndView mav = new ModelAndView();
		
		if (mService.login(m) == 1) {
			mav.addObject("sessionID", m.getM_id());
			if(m.getM_id().equals("admin")) {
				mav.setViewName("redirect:admin.go");
			} else {
				mav.setViewName("redirect:boardMain.go");
			}
		} else {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			if (mService.login(m) == 0) {
				out.println("<script>alert('가입하지 않은 사용자 입니다.'); location.href='login.go';</script>");
			} else if (mService.login(m) == 2) {
				out.println("<script>alert('비밀번호가 일치하지 않습니다.'); location.href='login.go';</script>");
			} else if (mService.login(m) == 3) {
				out.println("<script>alert('로그인의 실패하였습니다.'); location.href='login.go';</script>");
			}
			out.flush();
			return null;
		}
		return mav;
	}
	
	@RequestMapping(value="login.go", method = RequestMethod.GET)
	public ModelAndView login_main (HttpServletRequest request, HttpServletResponse response) {
		rsa.initRsa(request);
		SA.sessionAbandon(response);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", "member/login.jsp");
		mav.setViewName("main");
		
		return mav;
	}
	
	@RequestMapping("logout.do")
	public ModelAndView logoutDo (SessionStatus status) {
		
		ModelAndView mav = new ModelAndView();
		status.setComplete();
        mav.setViewName("redirect:/");
        return mav;
	}
	
	@RequestMapping(value="joinMember.go", method = RequestMethod.GET)
	public ModelAndView joinMember_go (HttpServletResponse response) {
		SA.sessionAbandon(response);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", "member/joinMember.jsp");
		mav.setViewName("main");
		
		return mav;
	}
	
	@RequestMapping(value="joinMember.do", method=RequestMethod.POST)
	public ModelAndView joinMemberDo (Cloud m) {
		mService.regMember(m);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/");
		return mav;
	}
	
	@RequestMapping("myInfo.go")
	public ModelAndView myInfoGo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		SA.sessionAbandon(response);
		ModelAndView mav = new ModelAndView();
		if (session.getAttribute("sessionID") != null) {
			String s_id = (String) session.getAttribute("sessionID");
			request.setAttribute("s", mService.selectMember(s_id));
			mav.addObject("page", "member/myInfo.jsp");
			mav.setViewName("main");
		} else {
			mav.setViewName("redirect:/");
		}
		
		return mav;
	}
	
	@RequestMapping("checkPW.go")
	public ModelAndView checkPW_Go(HttpServletRequest request, HttpServletResponse response, HttpSession session, Cloud sdto) {
		SA.sessionAbandon(response);
		ModelAndView mav = new ModelAndView();
		if (session.getAttribute("sessionID") != null) {
			String s_id = (String) session.getAttribute("sessionID");
			request.setAttribute("s", mService.selectMember(s_id));
			request.setAttribute("value", request.getParameter("value"));
			mav.addObject("page", "member/checkPW.jsp");
			mav.setViewName("main");
		} else {
			mav.setViewName("redirect:/");
		}
		
		return mav;
	}
	
	@RequestMapping(value="checkPW.do", method = RequestMethod.POST)
	public ModelAndView checkPW_Do (HttpServletRequest request, HttpServletResponse response, HttpSession session, Cloud sdto, @RequestParam("pw") String cpw, @RequestParam int value) throws Exception {
		ModelAndView mav = new ModelAndView();
		if(!sdto.getM_pw().equals(cpw)) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('비밀번호가 틀립니다.'); location.href='myInfo.go';</script>");
			out.flush();
			return null;
		} else {
			String s_id = (String) session.getAttribute("sessionID");
			request.setAttribute("s", mService.selectMember(s_id));
			if (value == 1) { // 회원정보 수정일 경우 비밀번호가 일치할 경우
				mav.setViewName("redirect:/updateMember.go");
			} else if (value == 2) { // 탈퇴 일 경우 비밀번호가 일치할 경우
				mav.setViewName("redirect:/dropMember.do");
			} else {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('잘못된 경로로 접근하셨습니다.'); location.href='myInfo.go';</script>");
				out.flush();
				return null;
			}
		}
		return mav;
	}
	
	@RequestMapping("updateMember.go")
	public ModelAndView updateMember_Go (HttpServletRequest request, HttpServletResponse response, HttpSession session, Cloud sdto) {
		SA.sessionAbandon(response);
		ModelAndView mav = new ModelAndView();
		if (session.getAttribute("sessionID") != null) {
			String s_id = (String) session.getAttribute("sessionID");
			request.setAttribute("s", mService.selectMember(s_id));
			mav.addObject("page", "member/updateMyInfo.jsp");
			mav.setViewName("main");
		} else {
			mav.setViewName("redirect:/");
		}
		
		return mav;
	}
	

	@RequestMapping(value="updateMemberInfo.do", method=RequestMethod.POST)
	public ModelAndView updateMemberDo(HttpSession session, Cloud sdto) {
		
		ModelAndView mav = new ModelAndView();
		if (session.getAttribute("sessionID") != null) {
			mService.updateMemberInfo(sdto);
			mav.setViewName("redirect:myInfo.go");
		} else {
			mav.setViewName("redirect:/");
		}
		return mav;
	}
	
	@RequestMapping("dropMember.do")
	public ModelAndView dropMemberDo (HttpServletResponse response, HttpSession session, Cloud sdto, SessionStatus status) throws Exception {
		
		if (session.getAttribute("sessionID") != null) {
			String s_id = (String) session.getAttribute("sessionID");
			sdto.setM_id(s_id);
			mService.deleteMember(sdto);
			mService.dropMemberTable(sdto);
			status.setComplete();
			
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('회원 탈퇴가 정상적으로 이루어졌습니다.'); location.href='login.go';</script>");
			out.flush();
			return null;
		} else {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('회원 탈퇴에 실패하셨습니다.'); location.href='myInfo.go';</script>");
			out.flush();
			return null;
		}
	}

	@RequestMapping("admin.go")
	public ModelAndView adminList (HttpServletRequest request, HttpServletResponse response, HttpSession session,
	                        @RequestParam(defaultValue="1") int curPage) throws Exception {
		SA.sessionAbandon(response);
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("sessionID") != null) {
			String s_id = (String) session.getAttribute("sessionID");
			if (!s_id.equals("admin")) {
				mav.setViewName("redirect:/");
			} else {
				// 레코드의 갯수 계산
				int count = mService.checkMember();

				// 페이지 나누기 관련 처리
				Pagination boardPager = new Pagination(count, curPage);
				int start = boardPager.getPageBegin();
				int end = boardPager.getPageEnd();
				
				List<Cloud> list = mService.listAllMembers(start, end);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("list", list); // list
				map.put("count", count); // 레코드의 갯수
				map.put("boardPager", boardPager);
				
				mav.addObject("map", map); // 맵에 저장된 데이터를 mav에 저장
				
				mav.addObject("headerPage", "board/header.jsp");
				mav.addObject("contentPage", "admin/admin.jsp");
				mav.setViewName("boardMain");
			}
			
		} else {
			mav.setViewName("redirect:/");
		}
		return mav;
	}
	
	@RequestMapping("adminDeleteMember.do")
	public ModelAndView adminDeleteMemberDo (HttpServletRequest request, HttpServletResponse response, HttpSession session, Cloud c, @RequestParam("m_id") String m_id) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		if (session.getAttribute("sessionID") != null) {
			String s_id = (String) session.getAttribute("sessionID");
			if (!s_id.equals("admin")) {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('회원 삭제 권한이 없습니다.'); location.href='login.go';</script>");
				out.flush();
				return null;
			} else {
				c.setM_id(m_id);
				mService.deleteMember(c);
				mService.dropMemberTable(c);
				
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('회원 삭제가 정상적으로 이루어졌습니다.'); location.href='admin.go';</script>");
				out.flush();
				return null;
			}
		} else {
			mav.setViewName("redirect:/");
		}
		return mav;
	}
	
	@RequestMapping("findPassword.go")
	public ModelAndView findPassword_Go (HttpServletResponse response, HttpSession session, Cloud c) {
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", "member/findPassword.jsp");
		mav.setViewName("main");
		
		return mav;

	}
	
	@RequestMapping(value="resetPassword.go", method=RequestMethod.POST)
	public ModelAndView resetPassword_GO (HttpServletRequest request, HttpServletResponse response, HttpSession session, Cloud c) throws Exception {
		SA.sessionAbandon(response);
		ModelAndView mav = new ModelAndView();
		if (mService.checkInfo(c) != 1) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('작성한 정보의 회원이 존재하지 않습니다.'); location.href='login.go';</script>");
			out.flush();
			return null;
		} else {
			request.setAttribute("id", c.getM_id());
			mav.addObject("page", "member/updatePassword.jsp");
			mav.setViewName("main");
		}
		return mav;
	}
	
	@RequestMapping(value="updatePW.do", method=RequestMethod.POST)
	public ModelAndView updatePW_Do (HttpServletResponse response, Cloud c, @RequestParam("m_id") String m_id,
								@RequestParam("m_pwc") String pwc, @RequestParam("m_pw") String pw) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		if (m_id != null) {
			if(!pwc.equals(pw)) {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('입력하신 비밀번호가 일치하지 않습니다.'); location.href='login.go';</script>");
				out.flush();
				return null;
			}
			c.setM_id(m_id);
			mService.updatePW(c);
			
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('성공적으로 비밀번호를 변경하였습니다.'); location.href='login.go';</script>");
			out.flush();
			return null;
		}
		
		return mav;
	}
	
	@RequestMapping(value="idcheck.do", produces ="application/json; charset=utf-8", method=RequestMethod.POST)
    @ResponseBody
    public Map<Object, Object> idcheck (@RequestBody String value) {
        int count = 0;
        Map<Object, Object> map = new HashMap<Object, Object>();
        if (mService.selectMember(value) != null) {
        	count = 1;
        }
        map.put("cnt", count);
        System.out.println("count : "+count);
 
        return map;
    }
}

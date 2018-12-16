package com.sol4.cloud.Controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sol4.cloud.Pagination;
import com.sol4.cloud.SessionAbandon;
import com.sol4.cloud.Domain.Cloud;
import com.sol4.cloud.Service.BoardService;
import com.sol4.cloud.Service.NoticeService;

@Controller
public class NoticeController {
	
	@Autowired
	private NoticeService nService;
	
	@Autowired
	private BoardService bService;
	
	private static final SessionAbandon SA = new SessionAbandon();
	
	@RequestMapping(value="noboardList.go")
	public ModelAndView noboardList (HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(defaultValue="all") String searchOption,
			@RequestParam(defaultValue="") String keyword, // 검색어
            @RequestParam(defaultValue="1") int curPage,  // 현재 페이지
            Cloud c) throws Exception {
		SA.sessionAbandon(response);
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("sessionID") != null) {
			if (keyword != null) {
				c.setF_name(keyword);
			} else {
				c.setF_name("");
			}
			
			// 레코드의 갯수 계산
		    int count = nService.checkNotice();

		    // 페이지 나누기 관련 처리
		    Pagination boardPager = new Pagination(count, curPage);
		    int start = boardPager.getPageBegin();
		    int end = boardPager.getPageEnd();
			
		    List<Cloud> list = nService.listAllNotice(start, end, searchOption, keyword);
		    
		    Map<String, Object> map = new HashMap<String, Object>();
		    map.put("list", list); // list
		    map.put("count", count); // 레코드의 갯수
		    map.put("searchOption", searchOption); // 검색옵션
		    map.put("keyword", keyword); // 검색키워드
		    map.put("boardPager", boardPager);
		    
		    mav.addObject("map", map); // 맵에 저장된 데이터를 mav에 저장
		    c.setM_id((String)session.getAttribute("sessionID"));
		    request.setAttribute("UC", (int)(((double)bService.usedCapacity(c)/2048)*100));
		    
			mav.addObject("headerPage", "board/header.jsp");
			mav.addObject("contentPage", "notice/no_board.jsp");
			mav.setViewName("boardMain");
		} else {
			mav.setViewName("redirect:/");
		}
		return mav;
	}
	
	@RequestMapping(value="noticeDetail")
	public ModelAndView noboardDetail (HttpServletRequest request, HttpSession session, @RequestParam("no_num") int no_num, Cloud c) throws Exception {
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("sessionID") != null) {
			c.setM_id((String)session.getAttribute("sessionID"));
			request.setAttribute("UC", (int)(((double)bService.usedCapacity(c)/2048)*100));
			System.out.println(no_num);
			c.setNo_num(no_num);
			nService.updateViewCnt(c);
			mav.addObject("detail", nService.getNotice(c));
			mav.addObject("headerPage", "board/header.jsp");
			mav.addObject("contentPage", "notice/no_detail.jsp");
			mav.setViewName("boardMain");
		}
		return mav;
	}
	
	@RequestMapping(value="noticeWrite.go")
	public ModelAndView noboardWrite (HttpServletRequest request, HttpServletResponse response, HttpSession session, Cloud c) throws Exception {
		SA.sessionAbandon(response);
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("sessionID") != null) {
			c.setM_id((String)session.getAttribute("sessionID"));
			request.setAttribute("UC", (int)(((double)bService.usedCapacity(c)/2048)*100));
			
			mav.addObject("list", nService.getShareFile(c));
			mav.addObject("headerPage", "board/header.jsp");
			mav.addObject("contentPage", "notice/no_write.jsp");
			mav.setViewName("boardMain");
		} else {
			mav.setViewName("redirect:/");
		}
		return mav;
	}
	
	@RequestMapping(value="noticeWrite.do", method = RequestMethod.POST)
	public ModelAndView noboardWrite_do (@RequestParam("no_class") int no_class, @RequestParam("no_title") String no_title,
			@RequestParam("no_content") String no_content, @RequestParam("no_file") String no_file, 
			HttpServletRequest request, HttpSession session, Cloud c) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("sessionID") != null) {
			c.setM_id((String)session.getAttribute("sessionID"));
			request.setAttribute("UC", (int)(((double)bService.usedCapacity(c)/2048)*100));
			
			c.setNo_writer((String)session.getAttribute("sessionID"));
			c.setNo_class(no_class);
			c.setNo_title(no_title);
			c.setNo_content(no_content);
			c.setNo_file(no_file);
			
			nService.writeNotice(c);
			
			mav.setViewName("redirect:noboardList.go");
		} else {
			mav.setViewName("redirect:/");
		}
		return mav;
	}
	
	
	@RequestMapping(value = "downloadShare", method = RequestMethod.GET)
	public ModelAndView download(@RequestParam("no_writer") String no_writer, @RequestParam("no_file") String no_file, Cloud c,
			HttpServletResponse response )throws Exception{
		
		String path = "C:\\SolCloud\\"+no_writer+"\\"+no_file;
		System.out.println(path);
		
		c.setNo_file(no_file);
		c.setNo_writer(no_writer);
		
		if (nService.checkFile(c) != 1) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('파일이 없습니다.'); location.href='noboardList.go';</script>");
			out.flush();
			return null;
		} else {
			File down = new File(path);
			return new ModelAndView("download","downloadFile",down);
		}
	}
	
	@RequestMapping("deleteNotice")
	public ModelAndView deleteNotice (HttpSession session, HttpServletRequest request, 
			HttpServletResponse response, @RequestParam("no_num") int no_num, Cloud c) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("sessionID") != null) {
			c.setM_id((String)session.getAttribute("sessionID"));
			request.setAttribute("UC", (int)(((double)bService.usedCapacity(c)/2048)*100));
			
			c.setNo_writer((String)session.getAttribute("sessionID"));
			c.setNo_num(no_num);
			
			nService.deleteNotice(c);
			
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('게시물 삭제가 정상적으로 이루어졌습니다.'); location.href='noboardList.go';</script>");
			out.flush();
			return null;
			
		} else {
			mav.setViewName("redirect:/");
		}
		return mav;
	}
}

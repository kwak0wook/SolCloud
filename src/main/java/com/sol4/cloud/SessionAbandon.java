package com.sol4.cloud;

import javax.servlet.http.HttpServletResponse;

public class SessionAbandon {
	
	public void sessionAbandon(HttpServletResponse response) {
		response.setHeader("pragma","No-cache"); 
		response.setHeader("Cache-Control","no-cache"); 
		response.addHeader("Cache-Control","No-store"); 
		response.setDateHeader("Expires",1L); 
	}
}

//package com.zifang.util.zex.demo.thirdpart.jar;
//
//import org.slf4j.log;
//import org.slf4j.logFactory;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Enumeration;
//
//public class CORSResponseFilter implements Filter {
//
//	private static log log = logFactory.getlog(CORSResponseFilter.class);
//
//	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//		HttpServletResponse response = (HttpServletResponse) res;
//		Enumeration<String> a  = ((HttpServletRequest)req).getHeaderNames();
//		while(a.hasMoreElements()) {
//			String header = a.nextElement();
//			log.info(header + ":" +((HttpServletRequest)req).getHeader(header));
//		}
//		log.info(((HttpServletRequest)req).getMethod());
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setHeader("Access-Control-Allow-Credentials", "true");
//		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
//		response.setHeader("Access-Control-Max-Age", "3600");
//		response.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, Accept-Language");
//		response.setHeader("Access-Control-Expose-Headers","X-Total-Count");
//		if (((HttpServletRequest)req).getMethod().equals("OPTIONS")) {
//			return;
//		} else {
//			chain.doFilter(req, res);
//		}
//	}
//	public void init(FilterConfig filterConfig) {}
//	public void destroy() {}
//}

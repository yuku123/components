package com.zifang.demo.designpatten.chain.tomcatfilter;

public class Main {
	
	
	public static void main(String[] args) {
		
		FilterChain chain = new FilterChain();
		//设置要执行的Servlet
		chain.setServlet(new Servlet());
		
		//添加过滤器
		chain.addFilter(new LogFilter());
		chain.addFilter(new EncodeFilter());
		
		//执行
		chain.doFilter(new Request(), new Response());
	}
}

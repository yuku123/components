package com.zifang.demo.designpatten.chain.tomcatfilter;

public class LogFilter implements Filter{

	@Override
	public void doFilter(Request req, Response res, FilterChain chain) {
		//在 servlet 之前执行
		System.out.println("log start ...");
		
		chain.doFilter(req, res);
		
		//在 servlet 之后执行
		System.out.println("log end ...");
	}

}

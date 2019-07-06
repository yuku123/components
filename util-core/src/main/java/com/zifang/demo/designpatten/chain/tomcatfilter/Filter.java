package com.zifang.demo.designpatten.chain.tomcatfilter;

public interface Filter {

	public void doFilter(Request req, Response res, FilterChain filterChain);
}

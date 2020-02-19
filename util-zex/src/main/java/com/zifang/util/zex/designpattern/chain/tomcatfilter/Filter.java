package com.zifang.util.zex.designpattern.chain.tomcatfilter;

public interface Filter {

	public void doFilter(Request req, Response res, FilterChain filterChain);
}

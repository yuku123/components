package com.zifang.util.zex.designpattern.chain.tomcatfilter;

public interface Filter {

	void doFilter(Request req, Response res, FilterChain filterChain);
}

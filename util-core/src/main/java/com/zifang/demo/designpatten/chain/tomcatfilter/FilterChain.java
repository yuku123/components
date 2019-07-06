package com.zifang.demo.designpatten.chain.tomcatfilter;

import java.util.ArrayList;
import java.util.List;

public class FilterChain{
	
	//目标servlet
	private Servlet servlet;
	//将要执行的过滤器下标（过滤器链）
	private int pos = 0;
	
	//存储过滤器的容器（tomcat中使用的是一个数组进行存储的）
	private List<Filter> list = new ArrayList<>();
	
	public void doFilter(Request req, Response res) {
		//判断是否还有要执行的filter
		if(pos<list.size()){
			//每次调用过滤器链的doFilter时，都要将pos坐标+1
			//注意pos++的位置，如果调用完doFilter在pos++ 就出现死循环了
			list.get(pos++).doFilter(req, res, this);
		}else{
			//如果所有的过滤器都执行完成则执行servlet
			getServlet().service(req, res);
		}
	}
	
	//向过滤器链中添加过滤器
	public void addFilter(Filter filter){
		list.add(filter);
	}
	
	//设置servlet
	public void setServlet(Servlet servlet) {
		this.servlet = servlet;
	}

	public Servlet getServlet() {
		return servlet;
	}

	
	
}

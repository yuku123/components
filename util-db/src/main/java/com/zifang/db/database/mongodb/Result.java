package com.zifang.db.database.mongodb;

import java.util.List;

public class Result {
	
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	private Integer pageCount;
	private List list;
}

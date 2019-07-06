package com.zifang.util.designpatten.chain.filter;

public class SesitiveFilter implements Filter {

	@Override
	public String doFilter(String str) {
		//process the sensitive words
		String r = str.replace("被就业", "就业")
			 .replace("敏感", "");
		
		return r;
	}

}

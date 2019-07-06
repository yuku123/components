package com.zifang.util.core.demo.jdk.java.net.bookdemo;

import java.net.*;

public class NoGovernmentCookies implements CookiePolicy {

	@Override
	public boolean shouldAccept(URI uri, HttpCookie cookie) {
		if (uri.getAuthority().toLowerCase().endsWith(".gov") || cookie.getDomain().toLowerCase().endsWith(".gov")) {
			return false;
		}
		return true;
	}
}
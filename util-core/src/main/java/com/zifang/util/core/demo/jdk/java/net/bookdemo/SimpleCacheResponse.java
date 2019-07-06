package com.zifang.util.core.demo.jdk.java.net.bookdemo;

import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleCacheResponse extends CacheResponse {

	private final Map<String, List<String>> headers;
	private final SimpleCacheRequest request;
	private final Date expires;
	private final CacheControl control;

	public SimpleCacheResponse(SimpleCacheRequest request, URLConnection uc, CacheControl control) throws IOException {

		this.request = request;
		this.control = control;
		this.expires = new Date(uc.getExpiration());
		this.headers = Collections.unmodifiableMap(uc.getHeaderFields());
	}

	@Override
	public InputStream getBody() {
		return new ByteArrayInputStream(request.getData());
	}

	@Override
	public Map<String, List<String>> getHeaders() throws IOException {
		return headers;
	}

	public CacheControl getControl() {
		return control;
	}

	public boolean isExpired() {
		Date now = new Date();
		if (control.getMaxAge().before(now))
			return true;
		else if (expires != null && control.getMaxAge() != null) {
			return expires.before(now);
		} else {
			return false;
		}
	}
}
package com.zifang.util.core.demo.jdk.java.net.bookdemo;

import java.io.*;
import java.net.*;
import java.util.*;

public class LocalProxySelector extends ProxySelector {

	private List<URI> failed = new ArrayList<URI>();

	public List<Proxy> select(URI uri) {

		List<Proxy> result = new ArrayList<Proxy>();
		if (failed.contains(uri) || !"http".equalsIgnoreCase(uri.getScheme())) {
			result.add(Proxy.NO_PROXY);
		} else {
			SocketAddress proxyAddress = new InetSocketAddress("proxy.example.com", 8000);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);
			result.add(proxy);
		}

		return result;
	}

	public void connectFailed(URI uri, SocketAddress address, IOException ex) {
		failed.add(uri);
	}
}
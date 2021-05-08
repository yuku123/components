package com.zifang.util.server.net.bookdemo;

import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;

public class NoGovernmentCookies implements CookiePolicy {

    @Override
    public boolean shouldAccept(URI uri, HttpCookie cookie) {
        return !uri.getAuthority().toLowerCase().endsWith(".gov") && !cookie.getDomain().toLowerCase().endsWith(".gov");
    }
}
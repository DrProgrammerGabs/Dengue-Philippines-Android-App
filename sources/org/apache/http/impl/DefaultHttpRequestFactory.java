package org.apache.http.impl;

import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.RequestLine;

@Deprecated
public class DefaultHttpRequestFactory implements HttpRequestFactory {
    public DefaultHttpRequestFactory() {
        throw new RuntimeException("Stub!");
    }

    public HttpRequest newHttpRequest(RequestLine requestline) throws MethodNotSupportedException {
        throw new RuntimeException("Stub!");
    }

    public HttpRequest newHttpRequest(String method, String uri) throws MethodNotSupportedException {
        throw new RuntimeException("Stub!");
    }
}

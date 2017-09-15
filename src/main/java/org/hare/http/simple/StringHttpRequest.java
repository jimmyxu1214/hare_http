package org.hare.http.simple;

import org.apache.http.client.ResponseHandler;
import org.hare.http.HttpRequest;
import org.hare.http.handler.StringResponseHandler;

public class StringHttpRequest extends HttpRequest<String>{

	public StringHttpRequest(String httpUrl) {
		super(httpUrl);
	}

	@Override
	public ResponseHandler<String> getResponseHandler() {
		return new StringResponseHandler();
	}
}

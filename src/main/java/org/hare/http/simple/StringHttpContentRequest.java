package org.hare.http.simple;

import org.apache.http.client.ResponseHandler;
import org.hare.http.HttpContentRequest;
import org.hare.http.handler.StringResponseHandler;

public class StringHttpContentRequest extends HttpContentRequest<String> {

	public StringHttpContentRequest(String httpUrl) {
		super(httpUrl);
	}

	@Override
	public ResponseHandler<String> getResponseHandler() {
		return new StringResponseHandler();
	}

	
	
}

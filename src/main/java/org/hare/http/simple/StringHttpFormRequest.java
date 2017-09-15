package org.hare.http.simple;

import org.apache.http.client.ResponseHandler;
import org.hare.http.HttpFormRequest;
import org.hare.http.handler.StringResponseHandler;

public class StringHttpFormRequest extends HttpFormRequest<String> {

	public StringHttpFormRequest(String httpUrl) {
		super(httpUrl);
	}

	@Override
	public ResponseHandler<String> getResponseHandler() {
		return new StringResponseHandler();
	}

	
}

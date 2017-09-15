package org.hare.http.simple;

import org.apache.http.client.ResponseHandler;
import org.hare.http.HttpMultipartRequest;
import org.hare.http.handler.StringResponseHandler;

public class StringHttpMultipartRequest extends HttpMultipartRequest<String> {

	public StringHttpMultipartRequest(String httpUrl) {
		super(httpUrl);
	}
	
	@Override
	public ResponseHandler<String> getResponseHandler() {
		return new StringResponseHandler();
	}

}

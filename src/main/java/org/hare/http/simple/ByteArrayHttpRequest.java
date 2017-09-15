package org.hare.http.simple;

import org.apache.http.client.ResponseHandler;
import org.hare.http.HttpRequest;
import org.hare.http.handler.ByteArrayResponseHandler;

public class ByteArrayHttpRequest extends HttpRequest<byte[]>{

	public ByteArrayHttpRequest(String httpUrl) {
		super(httpUrl);
	}
	
	@Override
	public ResponseHandler<byte[]> getResponseHandler() {
		return new ByteArrayResponseHandler();
	}

}

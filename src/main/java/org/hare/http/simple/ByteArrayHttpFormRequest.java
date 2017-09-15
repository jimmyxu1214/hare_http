package org.hare.http.simple;

import org.apache.http.client.ResponseHandler;
import org.hare.http.HttpFormRequest;
import org.hare.http.handler.ByteArrayResponseHandler;

public class ByteArrayHttpFormRequest extends HttpFormRequest<byte[]> {

	public ByteArrayHttpFormRequest(String httpUrl) {
		super(httpUrl);
	}

	@Override
	public ResponseHandler<byte[]> getResponseHandler() {
		return new ByteArrayResponseHandler();
	}

	
}

package org.hare.http.simple;

import java.io.InputStream;

import org.apache.http.client.ResponseHandler;
import org.hare.http.HttpRequest;
import org.hare.http.handler.InputStreamResponseHandler;

public class StreamHttpRequest extends HttpRequest<InputStream>{

	public StreamHttpRequest(String httpUrl) {
		super(httpUrl);
	}
	
	@Override
	public ResponseHandler<InputStream> getResponseHandler() {
		return new InputStreamResponseHandler();
	}

}

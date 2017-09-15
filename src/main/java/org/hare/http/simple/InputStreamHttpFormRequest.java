package org.hare.http.simple;

import java.io.InputStream;

import org.apache.http.client.ResponseHandler;
import org.hare.http.HttpFormRequest;
import org.hare.http.handler.InputStreamResponseHandler;

public class InputStreamHttpFormRequest extends HttpFormRequest<InputStream> {

	public InputStreamHttpFormRequest(String httpUrl) {
		super(httpUrl);
	}

	@Override
	public ResponseHandler<InputStream> getResponseHandler() {
		return new InputStreamResponseHandler();
	}

	
}

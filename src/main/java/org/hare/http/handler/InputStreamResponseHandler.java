package org.hare.http.handler;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

public class InputStreamResponseHandler implements ResponseHandler<InputStream> {

	@Override
	public InputStream handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		return response.getEntity().getContent();
	}

}

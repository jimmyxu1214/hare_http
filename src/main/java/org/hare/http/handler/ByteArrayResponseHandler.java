package org.hare.http.handler;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.hare.http.util.StreamUtil;

public class ByteArrayResponseHandler implements ResponseHandler<byte[]> {

	@Override
	public byte[] handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		
		byte[] result = null;
		try{
			HttpEntity entity = response.getEntity();
            if(entity != null){
            	result = StreamUtil.readByteArray(entity.getContent());
            }
        }finally{
        	if(response instanceof CloseableHttpResponse){
        		((CloseableHttpResponse)response).close();
        	}
        }
		return result;
	}

}

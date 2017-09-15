package org.hare.http.handler;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;


public class StringResponseHandler implements ResponseHandler<String> {
	
	private String charset = "UTF-8";
	
	public StringResponseHandler(String charset){
		if(charset != null){
			this.charset = charset;
		}
	}
	
	public StringResponseHandler(){
		
	}

	@Override
	public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		String result = null;
		try{
        	HttpEntity entity = response.getEntity();
            if(entity != null){
           	 	result = EntityUtils.toString(entity, charset);
            }
        }finally{
        	if(response instanceof CloseableHttpResponse){
        		((CloseableHttpResponse)response).close();
        	}
        }
		return result;
		
	}

}

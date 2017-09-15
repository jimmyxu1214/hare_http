package org.hare.http;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

public class HttpContentRequest<T> extends HttpEntityRequest<T>{

	//请求内容
	private String content;
	
	public HttpContentRequest(String httpUrl) {
		super(httpUrl);
	}
	
	@Override
	public HttpEntity getHttpEntity() {
		return new StringEntity(content, ContentType.create(getContentType(), getCharset()));
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	

}

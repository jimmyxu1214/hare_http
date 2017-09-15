package org.hare.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;

public class HttpEntityRequest<T> extends HttpRequest<T> {

	private HttpEntity httpEntity;
	
	public HttpEntityRequest(String httpUrl) {
		super(httpUrl);
	}

	@Override
	protected void createHttpEntity(HttpRequestBase httpRequest) {
		if(HttpMethod.POST != getHttpMethod() && HttpMethod.PUT != getHttpMethod()){
			throw new HttpException("请求调用异常: HttpEntityRequest中HttpMethod 只能取POST/PUT");
		}
		HttpEntity httpEntity = getHttpEntity();
		if(httpEntity == null){
			throw new HttpException("请求调用异常: 请求体Entity未设置");
		}
		HttpEntityEnclosingRequestBase httpEntityRequest = (HttpEntityEnclosingRequestBase)httpRequest;
		httpEntityRequest.setEntity(httpEntity);
		if(isDebug()){
			logger.info("Http Entity 内容为:");
			debug(httpEntity);
		}
	}
	
	/**
	 * 不同的HttpEntity展示方式不同需要子类实现
	 * @param httpEntity
	 */
	protected void debug(HttpEntity httpEntity){
		
	}

	public HttpEntity getHttpEntity() {
		return httpEntity;
	}

	public void setHttpEntity(HttpEntity httpEntity) {
		this.httpEntity = httpEntity;
	}
	
	

}

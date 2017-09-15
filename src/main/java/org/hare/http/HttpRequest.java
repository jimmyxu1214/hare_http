package org.hare.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest<T> implements Request<T>{
	
	protected static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
	
	private String httpUrl;
	private String contentType;
	private int httpMethod = HttpMethod.POST;
	private RequestConfig requestConfig = RequestConfig.DEFAULT;
	private String charset = "UTF-8";
	private String querySeparator = "&";
	private Map<String, Object> queryMap = new LinkedHashMap<String, Object>();
	private Map<String, Object> headerMap = new  LinkedHashMap<String, Object>();
	private ResponseHandler<T> responseHandler;
	private boolean debug = false;
	
	public HttpRequest(String httpUrl){
		this.httpUrl = httpUrl;
	}
	
	public T execute(CloseableHttpClient httpClient){
		if(getResponseHandler() == null){
			throw new HttpException("请求调用异常: 返回值ResponseHandler未设置");
		}
		HttpRequestBase httpRequest = createRequest(getCompleteHttpUrl());
		httpRequest.setConfig(getRequestConfig());
		for (Map.Entry<String, Object> header : getHeaderMap().entrySet()) {
			httpRequest.setHeader(header.getKey(), header.getValue().toString());
		}
		if(debug){
			logger.info("Http Url 内容为:");
			logger.info(getCompleteHttpUrl());
			Header[] headers = httpRequest.getAllHeaders();
			StringBuffer headerBuffer = new StringBuffer();

			for(Header header : headers){
				headerBuffer.append("&")
							.append(header.getName())
							.append("=")
							.append(header.getValue());
			}
			if(headerBuffer.length() > 0){
				logger.info("Http Header 内容为:");
				logger.info(headerBuffer.substring(1));
			}
		}
		try {
			HttpContext context = HttpClientContext.create();
			return httpClient.execute(httpRequest, getResponseHandler(), context);
		} catch (Exception e) {
			logger.error("Http request error, Error message is {}", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public void addHeader(String headerName, String headerValue){
		headerMap.put(headerName, headerValue);
	}
	
	public void addQuery(String queryName, String queryValue){
		queryMap.put(queryName, queryValue);
	}
	
	public String getQueryString(){
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for(Map.Entry<String, Object> entry : queryMap.entrySet()){
			if(i > 0){
				sb.append(querySeparator);
			}
			try {
				sb.append(URLEncoder.encode(entry.getKey(), charset))
				  .append("=")
				  .append(URLEncoder.encode(entry.getValue().toString(), charset));
			} catch (UnsupportedEncodingException e) {
				logger.error("{} url encode error", entry.getValue());
			}
			i++;
		}
		return sb.toString();
	}
	
	public HttpRequestBase createRequest(String url){
		HttpRequestBase httpRequest = null;
		switch(httpMethod){
			case HttpMethod.GET : httpRequest = new HttpGet(url); break;
			case HttpMethod.PUT : httpRequest = new HttpPut(url); break;
			case HttpMethod.DELETE : httpRequest = new HttpDelete(url); break;
			default:
				httpRequest = new HttpPost(url);
		}
		createHttpEntity(httpRequest);
		return httpRequest;
	}
	
	protected void createHttpEntity(HttpRequestBase httpRequest){
		
	}
	
	public String getCompleteHttpUrl(){
		String completeHttpUrl = null;
		if(queryMap.size() > 0){
			if(httpUrl.contains("?")){
				completeHttpUrl = httpUrl + querySeparator + getQueryString();
			}else{
				completeHttpUrl = httpUrl + "?" + getQueryString();
			}
		}else{
			completeHttpUrl = httpUrl;
		}
		logger.debug(completeHttpUrl);
		return completeHttpUrl;
	}
	
	protected void log(InputStream in){
		BufferedReader br = null;
		try{
			StringBuffer buffer = new StringBuffer();
			br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while((line = br.readLine()) != null){
				buffer.append(line);
			}
			logger.info(buffer.toString());
			
		}catch(Exception e){
			if(br != null){
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public RequestConfig getRequestConfig() {
		return requestConfig;
	}

	public void setRequestConfig(RequestConfig requestConfig) {
		this.requestConfig = requestConfig;
	}

	public Map<String, Object> getQueryMap() {
		return queryMap;
	}

	public void setQueryMap(Map<String, Object> queryMap) {
		this.queryMap = queryMap;
	}

	public Map<String, Object> getHeaderMap() {
		return headerMap;
	}

	public void setHeaderMap(Map<String, Object> headerMap) {
		this.headerMap = headerMap;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getQuerySeparator() {
		return querySeparator;
	}

	public void setQuerySeparator(String querySeparator) {
		this.querySeparator = querySeparator;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public int getHttpMethod() {
		return httpMethod;
	}
	
	public void setHttpMethod(int httpMethod) {
		this.httpMethod = httpMethod;
	}
	
	public void setResponseHandler(ResponseHandler<T> responseHandler) {
		this.responseHandler = responseHandler;
	}

	public ResponseHandler<T> getResponseHandler() {
		return responseHandler;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	

}

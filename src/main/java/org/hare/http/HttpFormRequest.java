package org.hare.http;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

public class HttpFormRequest<T> extends HttpEntityRequest<T> {
	
	private Map<String, Object> formMap = new HashMap<String, Object>();
	private String encoding = "UTF-8";

	public HttpFormRequest(String httpUrl) {
		super(httpUrl);
	}

	@Override
	public HttpEntity getHttpEntity() {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (Map.Entry<String, Object> entry : formMap.entrySet()) {
			nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
		}
		try {
			return new UrlEncodedFormEntity(nvps, encoding);
		} catch (UnsupportedEncodingException e) {
			logger.error("Form UrlEncodedFormEntity Error:{}", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public void addFormData(String dataName, String dataValue) {
		formMap.put(dataName, dataValue);
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Map<String, Object> getFormMap() {
		return formMap;
	}

	public void setFormMap(Map<String, Object> formMap) {
		this.formMap = formMap;
	}
	
	

	
	
}

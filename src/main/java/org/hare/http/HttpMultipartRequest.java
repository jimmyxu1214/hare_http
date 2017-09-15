package org.hare.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

public class HttpMultipartRequest<T> extends HttpEntityRequest<T>{

	private List<Attachment> attachments = new ArrayList<Attachment>();
	
	public HttpMultipartRequest(String httpUrl) {
		super(httpUrl);
	}
	
	@Override
	public HttpEntity getHttpEntity() {
		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
		for(Attachment attachment : attachments){
			multipartEntityBuilder.addBinaryBody(attachment.getName(), attachment.getInput(), ContentType.create(attachment.getContentType()), attachment.getFileName());
		}
		return multipartEntityBuilder.build();
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	
}

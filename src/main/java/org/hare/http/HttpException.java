package org.hare.http;

public class HttpException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HttpException() {
		super();
	}

	public HttpException(String error, Throwable e) {
		super(error, e);
	}

	public HttpException(String error) {
		super(error);
	}

	public HttpException(Throwable e) {
		super(e);
	}
	

}

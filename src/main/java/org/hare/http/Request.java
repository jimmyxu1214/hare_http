package org.hare.http;

import org.apache.http.impl.client.CloseableHttpClient;

public interface Request<T> {

	T execute(CloseableHttpClient httpClient);
}

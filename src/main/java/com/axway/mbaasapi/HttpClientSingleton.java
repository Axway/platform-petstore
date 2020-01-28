package com.axway.mbaasapi;

import com.squareup.okhttp.OkHttpClient;

/**
 * Singleton class on httpclient.
 *
 */
public class HttpClientSingleton extends OkHttpClient {
	private static final HttpClientSingleton mInstance = new HttpClientSingleton();

	/**
	* 
	*/
	private HttpClientSingleton() {
		super();
	}

	public static final HttpClientSingleton getInstance() {
		return mInstance;
	}
}

/**
 * 
 */
package com.hoothub.android.client.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.util.Log;

import com.hoothub.android.Hoothub;
import com.hoothub.android.client.errors.BadRequest;
import com.hoothub.android.client.errors.ClientRestException;
import com.hoothub.android.client.errors.NotAuthorized;
import com.hoothub.android.client.errors.ResourceNotFound;
import com.hoothub.android.client.errors.ServerError;
import com.hoothub.android.client.errors.UnknownResponse;
import com.hoothub.android.client.parsers.HoothubParser;
import com.hoothub.android.client.types.HoothubType;
import com.hoothub.android.client.utils.JSONUtils;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class REST {
	
	static final String TAG = "REST";
	static final int TIMEOUT = 60;

	String mBaseUrl;
	HttpClient mHttpClient;
	Map<String, String> mDefaultHeaders = new HashMap<String, String>();
	
	public REST(String baseUrl) {
		mBaseUrl = baseUrl;
	}
	
	/*
	 * HTTP Methods
	 */

	public HoothubType get(String path, HoothubParser<? extends HoothubType> parser, NameValuePair ...params) throws ClientRestException, ClientProtocolException, IOException {
		String uri = buildUri(path);
		
		if (Hoothub.DEBUG) {
			Log.i(TAG, "GET " + uri);
		}
		
		HttpGet request = buildGetRequest(uri, params);
		
		String response = executeRequest(request);
		
		try {
			return JSONUtils.consume(response, parser);
		} catch (JSONException exception) {
			throw new ServerError(exception.getMessage());
		}
	}
	
	public void post(String path, NameValuePair ...params) throws ClientRestException, ClientProtocolException, IOException {
		String uri = buildUri(path);
		
		if (Hoothub.DEBUG) {
			Log.i(TAG, "POST " + uri);
		}
		
		HttpPost request = buildPostRequest(uri, params);
		if (request != null) {
			executeRequest(request);
		} else {
			throw new BadRequest();
		}
	}
	
	public HoothubType post(String path, HoothubParser<? extends HoothubType> parser, NameValuePair ...params) throws ClientRestException, ClientProtocolException, IOException {
		String uri = buildUri(path);
		
		if (Hoothub.DEBUG) {
			Log.i(TAG, "POST " + uri);
		}
		
		HttpPost request = buildPostRequest(uri, params);
		if (request != null) {
			String response = executeRequest(request);
			try {
				return JSONUtils.consume(response, parser);
			} catch (JSONException exception) {
				throw new ServerError(exception.getMessage());
			}
		} else {
			throw new BadRequest();
		}
	}
	
	public Map<String, String> getDefaultHeaders() {
		return mDefaultHeaders;
	}
	
	public void setDefaultHeaders(Map<String, String> headers) {
		mDefaultHeaders = headers;
	}
	
	public void addDefaultHeader(String header, String value) {
		mDefaultHeaders.put(header, value);
	}
	
	public void removeDefaultKey(String header) {
		mDefaultHeaders.remove(header);
	}
	
	/*
	 * Utility methods
	 */

	private List<NameValuePair> arrayToList(NameValuePair[] params) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for(NameValuePair param: params) {
			if (param.getValue() != null) {
				list.add(param);
			}
		}
		return list;
	}
	
	private HttpGet buildGetRequest(String uri, NameValuePair[] params) {
		String query = URLEncodedUtils.format(arrayToList(params), HTTP.UTF_8);
		HttpGet getRequest = new HttpGet(uri + "?" + query);
		
		return (HttpGet) finishRequestBuild(getRequest);
	}
	
	private HttpPost buildPostRequest(String uri, NameValuePair[] params) {
		HttpPost postRequest = new HttpPost(uri);
		try {
			postRequest.setEntity(new UrlEncodedFormEntity(arrayToList(params), HTTP.UTF_8));
		} catch (UnsupportedEncodingException exception) {
			exception.printStackTrace();
		}
		return (HttpPost) finishRequestBuild(postRequest);
	}
	
	private HttpRequestBase finishRequestBuild(HttpRequestBase request) {
		if (!mDefaultHeaders.isEmpty()) {
			for (String header : mDefaultHeaders.keySet()) {
				request.addHeader(header, mDefaultHeaders.get(header));
			}
		}
		return request;
	}

	private String executeRequest(HttpRequestBase request) throws ClientRestException, ClientProtocolException, IOException {
		final HttpClient client = getHttpClient();
		
		HttpResponse response = client.execute(request);
		
		switch(response.getStatusLine().getStatusCode()) {
		case 200:
		case 201:
			String result = EntityUtils.toString(response.getEntity());
			if (Hoothub.DEBUG) {
				Log.i(TAG, "Response: " + result);
			}
			return result;
		case 400:
			throw new BadRequest(EntityUtils.toString(response.getEntity()));
		case 401:
			throw new NotAuthorized(EntityUtils.toString(response.getEntity()));
		case 404:
			throw new ResourceNotFound(EntityUtils.toString(response.getEntity()));
		case 500:
		case 501:
		case 502:
		case 503:
		case 504:
			throw new ServerError(
				"Code: " + new Integer(response.getStatusLine().getStatusCode()).toString() +
				"\nBody: " + EntityUtils.toString(response.getEntity())
			);
		default:
			throw new UnknownResponse(
				"Code: " + new Integer(response.getStatusLine().getStatusCode()).toString() +
				"\nBody: " + EntityUtils.toString(response.getEntity())
			);
		}
	}

	private String buildUri(String path) {
		StringBuilder uri = new StringBuilder();
		uri.append(mBaseUrl).append(path);
		return uri.toString();
	}

	public synchronized HttpClient getHttpClient() {
		if(mHttpClient == null) {
			final SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
			
			final HttpParams httpParams = new BasicHttpParams();
			
			HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);

	        HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT * 1000);
	        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT * 1000);
	        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
			
			final ClientConnectionManager clientConnectionManager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
			
			mHttpClient = new DefaultHttpClient(clientConnectionManager, httpParams);
		}
		return mHttpClient;
	}
	
	public void setHttpClient(HttpClient client) {
		mHttpClient = client;
	}
}

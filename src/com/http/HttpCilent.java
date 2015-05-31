package com.http;

import java.net.URI;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.ff.widgets.WaitingPointBar;


public class HttpCilent {
	
	private String result;
	private String url;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String HttpGet() {
		HttpClient client = new DefaultHttpClient();
		try {
			HttpGet httpGet = new HttpGet(new URI(url));
			HttpResponse httpResponse = client.execute(httpGet);

			HttpEntity httpEntity = httpResponse.getEntity();
			result = EntityUtils.toString(httpEntity);
			
			if(result.startsWith("<!DOCTYPE html PUBLIC")
					||result.startsWith("<html><head><title>Apache Tomcat/6.0.18")){
				result = WaitingPointBar.MESSAGE_TEXT_ERROR_THE_SERVER_CAN_NOT_ACCESS;
			}
		
		} catch (Exception e) {
			if(e instanceof java.net.UnknownHostException){
				result = WaitingPointBar.MESSAGE_TEXT_ERROR_UNKNOWN_HOST_EXCEPTION;
			}
			e.printStackTrace();
		}
				 //执行完毕后给handler发送一个空消息
//				 handler.sendEmptyMessage(0);
		return result;
	}

	public void HttpPost(String url, List<NameValuePair> nameValuePairs) {
		try {
			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
					nameValuePairs);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(requestHttpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			String out = EntityUtils.toString(httpEntity);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void PostPut(String url, List<NameValuePair> nameValuePairs) {
		try {
			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
					nameValuePairs);
			HttpPut httpPut = new HttpPut(url);

			httpPut.setEntity(requestHttpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPut);

			HttpEntity httpEntity = httpResponse.getEntity();
			String out = EntityUtils.toString(httpEntity);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void PostDelete(String url, List<NameValuePair> nameValuePairs) {
		try {
			// HttpEntity requestHttpEntity = new
			// UrlEncodedFormEntity(nameValuePairs);
			HttpDelete httpDelete = new HttpDelete(url);
			// httpDelete.setEntity(requestHttpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpDelete);
			HttpEntity httpEntity = httpResponse.getEntity();
			String out = EntityUtils.toString(httpEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
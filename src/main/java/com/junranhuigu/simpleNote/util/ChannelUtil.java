package com.junranhuigu.simpleNote.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 工具类
 * */
public class ChannelUtil {
	/**
	 * http POST方式请求交互
	 * @param uri 请求的路径
	 * @param params 需要发送的参数内容
	 * @param isJson 发送内容是否是json形式
	 * @return 请求地址返回的结果
	 * */
	public static String httpPost(String uri, String params) throws Exception{
		URL url = new URL(uri);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.getRequestProperty("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("charset", "utf-8");
		connection.setReadTimeout(30000);
		connection.setConnectTimeout(30000);
		
		DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
		dos.writeBytes(params);
		dos.flush();
		dos.close();

		connection.connect();
		
		String line = null;
		BufferedReader bufferedReader = null;
		StringBuilder sb = new StringBuilder();
		InputStreamReader streamReader = null;
		try {
			streamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");
		} catch (IOException e) {
			streamReader = new InputStreamReader(connection.getErrorStream(), "UTF-8");
		} finally {
			if (streamReader != null) {
				bufferedReader = new BufferedReader(streamReader);
				sb = new StringBuilder();
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
				}
				streamReader.close();
			}
		}
		return sb.toString();
	}
	
	public static String httpGet(String uri, String charset) throws Exception{
		URL url = new URL(uri);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.getRequestProperty("GET");
		connection.setDoInput(true);
		connection.setDoOutput(false);
		connection.setRequestProperty("charset", charset);
		connection.setReadTimeout(30000);
		connection.setConnectTimeout(30000);

		connection.connect();
		
		String line = null;
		BufferedReader bufferedReader = null;
		StringBuilder sb = new StringBuilder();
		InputStreamReader streamReader = null;
		try {
			streamReader = new InputStreamReader(connection.getInputStream(), charset);
		} catch (IOException e) {
			streamReader = new InputStreamReader(connection.getErrorStream(), charset);
		} finally {
			if (streamReader != null) {
				bufferedReader = new BufferedReader(streamReader);
				sb = new StringBuilder();
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
				}
				streamReader.close();
			}
		}
		return sb.toString();
	}
	
	/**
	 * 获取得到排序好的网址字符串
	 * @param params 请求参数
	 * @return
	 */
	public static String getSortQueryString(Map<String,String> params) throws Exception {
		Object[] keys = params.keySet().toArray();
		Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(Object key : keys){
			sb.append(String.valueOf(key)).append("=").append(params.get(String.valueOf(key))).append("&");
		}
		
		String text = sb.toString();
		if(text.endsWith("&")) {
			text=text.substring(0,text.length()-1);
		}
		return text;
	}
	
	public static String httpsGet(String path, boolean isJson) throws Exception{
		TrustManager[] managers = {new ChannelUtil().provideTrust()};
		SSLContext context = SSLContext.getInstance("SSL", "SunJSSE");
		context.init(null, managers, new SecureRandom());
		
		HttpsURLConnection connection = (HttpsURLConnection) new URL(path).openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(false);
		connection.setConnectTimeout(3000);
		connection.setSSLSocketFactory(context.getSocketFactory());
		
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
		connection.setRequestProperty("charset", "utf-8");
		if(isJson){
			connection.setRequestProperty("content-type", "application/json");
		}
		
		String line = null;
		BufferedReader bufferedReader = null;
		StringBuilder sb = new StringBuilder();
		InputStreamReader streamReader = null;
		try {
			streamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");
		} catch (IOException e) {
			streamReader = new InputStreamReader(connection.getErrorStream(), "UTF-8");
		} finally {
			if (streamReader != null) {
				bufferedReader = new BufferedReader(streamReader);
				sb = new StringBuilder();
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
				}
				streamReader.close();
			}
		}
		return sb.toString();
	}
	
	private PrivateTrust provideTrust(){
		return new PrivateTrust();
	}
	
	private class PrivateTrust implements X509TrustManager{
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
	
}

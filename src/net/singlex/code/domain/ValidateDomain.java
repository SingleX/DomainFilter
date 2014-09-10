package net.singlex.code.domain;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ValidateDomain{
	
	Boolean flag = false;
	CloseableHttpClient httpClient = null;
	
	/**
	 * 调用百度APP API查询Whoise是否已被注册
	 * @param uri
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public boolean isAvailFromDuapp(String uri) throws ClientProtocolException, IOException{
		httpClient = HttpClients.createDefault();

		String url = "http://domainsearch.duapp.com/do.php?domain=" + uri.substring(24, uri.length());
//		String url = "http://domainsearch.duapp.com/do.php?domain=baidu.com";
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
		
		HttpEntity entity = httpResponse.getEntity();
		String content = EntityUtils.toString(entity);
//		System.out.println(content);
		Pattern pattern = Pattern.compile("com\\|1");
		Matcher m = pattern.matcher(content);
		if(m.find()){
//			System.out.println("yes.....");
			flag = true;
		}else{
//			System.out.println("no......");
			flag = false;
		}
		httpClient.close();
		return flag;
	}
	
	/**
	 * 调用万网API查询Whoise是否已被注册
	 * @param uri
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public boolean isAvailFromWanwang(String uri) throws ClientProtocolException, IOException{
		httpClient = HttpClients.createDefault();

		String url = "http://whois.www.net.cn/whois/api_whois?host=" + uri.substring(24, uri.length());
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
		
		HttpEntity entity = httpResponse.getEntity();
		String content = EntityUtils.toString(entity);
		System.out.println(content);
		Pattern pattern = Pattern.compile("\"code\":\"1000\"");
		Matcher m = pattern.matcher(content);
		if(m.find()){
			flag = true;
		}else{
		}
		httpClient.close();
		return flag;
	}
	
	/**
	 * 调用万网另一个API查询
	 * @param uri
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public boolean isAvailFromNet(String uri) throws ClientProtocolException, IOException{
		httpClient = HttpClients.createDefault();

		String url = "http://panda.www.net.cn/cgi-bin/check.cgi?area_domain=" + uri.substring(24, uri.length());
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
		
		HttpEntity entity = httpResponse.getEntity();
		String content = EntityUtils.toString(entity);
//		System.out.println(content);
		Pattern pattern = Pattern.compile("210");
		Matcher m = pattern.matcher(content);
		if(m.find()){
			flag = true;
			System.out.println("------210 true");
		}else{
			System.out.println("------21x false");
		}
		httpClient.close();
		return flag;
	}
	
	
	
}

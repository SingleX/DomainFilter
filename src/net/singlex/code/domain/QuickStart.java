package net.singlex.code.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class QuickStart {
	
	static String url = "http://del.chinaz.com/?";
	static String param = "p=0&ds[]=1&sort=1&suffix[]=com&dt=1&date=1&pagesize=30";
	
	static CloseableHttpClient httpClient = null;
	
	static ExportExcel exportExcel = new ExportExcel();
	static ValidateDomain validateDomain = new ValidateDomain();
	static int k = 0;
	static ArrayList<String> availList = new ArrayList<String>();
	static ArrayList<String> unavailList = new ArrayList<String>();
	
	static long start,current,end;
	
	public static void main(String[] args) throws IOException {
		
		QuickStart q = new QuickStart();
		
		start = System.currentTimeMillis();
		httpClient = HttpClients.createDefault();
			
		int num = q.doGetNum("http://del.chinaz.com/?p=0&ds[]=1&sort=1&suffix[]=com&dt=1&date=1&pagesize=30");
		
		for (int i = 0; i < 500; i++) {
			q.doGet(url, param, i);
			System.out.println();
		}
		
		httpClient.close();
		System.out.println("-----------summary------------");
		end = System.currentTimeMillis();
		String cost = (double)(end-start)/1000 + "s";
		System.out.println("Start Time: " + new java.util.Date(start));
		System.out.println("End   Time: " + new java.util.Date(end));
		System.out.println("Time  Cost: " + cost);
		
		System.out.println("Avail     : " + availList.size());
		System.out.println("unAvail   : " + unavailList.size());
		exportExcel.exportToExcel(availList);
	}
	
	public void doGet(String url, String param, int page) throws ClientProtocolException, IOException{
		
		HttpGet httpGet = new HttpGet(url+param+"&page="+page);
//		httpGet.addHeader(new BasicHeader("", ""));
//		httpGet.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.0 Safari/537.36");
		
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
		try {
			System.out.println(httpResponse.getStatusLine());
			HttpEntity entity = httpResponse.getEntity();
			String content = EntityUtils.toString(entity);
//			System.out.println(content);
			System.out.println("-----------------");
			doMatch(content, "http://whois.chinaz.com/\\w*\\.com", page);
		} catch (Exception e) {
		} finally {
			httpResponse.close();
		}
	}
	
	public int doGetNum(String url) throws ClientProtocolException, IOException{
		HttpGet httpGet = new HttpGet(url);
		
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
		try {
			System.out.println(httpResponse.getStatusLine());
			HttpEntity entity = httpResponse.getEntity();
			String content = EntityUtils.toString(entity);
			Pattern pattern = Pattern.compile("共 <b>\\d+</b> 页");
			Matcher m = pattern.matcher(content);
			if(m.find()){
				System.out.println("TotalPageNum -> " + m.group());
				String result = m.group();
				String x = m.group().substring(5, result.length()-6);
				return Integer.parseInt(x);
			}
		} catch (Exception e) {
		} finally {
			httpResponse.close();
		}
		return 1000;
	}
	
	public void doMatch(String content, String regex, int page) throws ClientProtocolException, IOException{
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(content);
		while(m.find()){
			if(validateDomain.isAvailFromNet(m.group())){
				++k;
				System.out.println("Time Cost: " + (double)(System.currentTimeMillis()-start)/1000 + "s\tFound: " + k + " -> " + m.group());
				availList.add(m.group());
			}else{
				System.out.println("Time Cost: " + (double)(System.currentTimeMillis()-start)/1000 + "s");
//				unavailList.add(m.group());
			}
		}
		System.out.println("Total: "+ k + "\tPage: " + page + "\t" + new java.util.Date(System.currentTimeMillis()));
	}
	
	public void doPost() throws ClientProtocolException, IOException{
		String url = "";
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", "admin"));
		nvps.add(new BasicNameValuePair("password", "123456"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse response = httpClient.execute(httpPost);
		try {
			System.out.println(response.getStatusLine());
			System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
		} finally {
			response.close();
		}
	}
}

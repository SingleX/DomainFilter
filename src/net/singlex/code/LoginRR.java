package net.singlex.code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class LoginRR {
	private CloseableHttpClient httpclient;
	private HttpPost httppost;// 用于提交登陆数据
	private HttpGet httpget;// 用于获得登录后的页面
	private String login_success;// 用于构造上面的HttpGet

	public LoginRR() {
		httpclient = HttpClients.createDefault();
		// 人人的登陆界面网址
		httppost = new HttpPost(
				"http://localhost:8080/j_spring_security_check");
	}

	public void logIn(String name, String password) throws Exception {
		// 打包将要传入的参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("j_username", name));
		params.add(new BasicNameValuePair("j_password", password));
		httppost.setEntity(new UrlEncodedFormEntity(params));

		try {
			// 提交登录数据
			HttpResponse re = httpclient.execute(httppost);
			// 获得跳转的网址
			Header locationHeader = re.getFirstHeader("Location");
			// 登陆不成功
			if (locationHeader == null) {
				System.out.println("登陆不成功，请稍后再试!");
				return;
			} else// 成功
			{
				login_success = locationHeader.getValue();// 获取登陆成功之后跳转链接
				System.out.println("登录成功,转向到：" + login_success);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void PrintText() throws IOException {
		httpget = new HttpGet(login_success);
		HttpResponse re2 = null;

		try {
			re2 = httpclient.execute(httpget);
			// 输出登录成功后的页面
			String str = EntityUtils.toString(re2.getEntity());
//			System.out.println(str);
			getvalue(str);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			httppost.abort();
			httpget.abort();
			httpclient.close();
		}
	}
	
	public void getvalue(String html){
		Document document = Jsoup.parse(html);
		Element element = document.getElementById("alertcenter");
		System.out.println(element);
		for (Element e : element.select("div.leftbox")) {
			System.out.println(e.attr("class"));
		}
		
	}

	public static void main(String[] args) throws Exception {
		String name = "hqadmin", password = "hqadmin";
		// 自己的账号，口令
		LoginRR lr = new LoginRR();
		lr.logIn(name, password);
		lr.PrintText();
	}
}
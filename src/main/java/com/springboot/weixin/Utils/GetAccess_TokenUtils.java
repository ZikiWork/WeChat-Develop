package com.springboot.weixin.Utils;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GetAccess_TokenUtils {

	/**
	 * 返回access_token！
	 * 
	 * @return access_token
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getAccess_Token() throws ClientProtocolException, IOException {
		// (1) 创建HttpGet实例
		HttpGet get = new HttpGet(
				"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxc0c13ad98f89debf&secret=2dc929086fc4e47203fcddd7ddbf6d14");
		// (2) 使用HttpClient发送get请求，获得返回结果HttpResponse
		HttpClient http = new DefaultHttpClient();
		HttpResponse response = http.execute(get);
		// (3) 读取返回结果s
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			// InputStream in = entity.getContent();
			String result = EntityUtils.toString(entity);
			System.out.println(result);
			// 获取JSON格式的字符串各个属性对应的值
			ObjectMapper mapper = new ObjectMapper();
			Map map = mapper.readValue(result, Map.class);
			String access_token = map.get("access_token").toString();
			String expires_in = map.get("expires_in").toString();
			System.out.println(access_token);
			return access_token;
		}
		return "";
	}
	
	
}

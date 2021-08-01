package com.springboot.weixin.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.weixin.Utils.CreateJsonUtils;
import com.springboot.weixin.Utils.GetAccess_TokenUtils;
import com.springboot.weixin.Utils.MessageUtils;

@RestController
public class WeiXinController {

	/**
	 * 设置基础配置时，获取信息
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @return
	 */
	@GetMapping("weixin")
	public String get(String signature, String timestamp, String nonce, String echostr) {// 得到微信发送的参数！
		System.out.println(signature);
		System.out.println(timestamp);
		System.out.println(nonce);
		System.out.println(echostr);
		// 填写对应的token！
		String token = "zyx";
		List<String> message = new ArrayList<>();
		message.add(token);
		message.add(timestamp);
		message.add(nonce);
		// 按照字典排序！
		Collections.sort(message);
		String newmessage = message.get(0) + message.get(1) + message.get(2);
		System.out.println(newmessage);
		// hash加密
		String sha1 = DigestUtils.sha1Hex(newmessage);
		System.out.println(sha1);
		if (sha1.equals(signature)) {
			return echostr;
		} else {
			return "";
		}
	}

	/**
	 * 自定义回复消息
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws DocumentException
	 */
	@PostMapping("weixin")
	@ResponseBody
	public String getText(HttpServletRequest request)
			throws UnsupportedEncodingException, IOException, DocumentException {
		// BufferedReader br = new BufferedReader(new
		// InputStreamReader(request.getInputStream(), "utf-8"));
		// 获取请求！（得到用户发送的信息！）
		Map<String, String> map = MessageUtils.xmlToMap(request);// 得到xml 转换为Map类型数据！
		for (Map.Entry<String, String> enrty : map.entrySet()) {
			System.out.println("key=" + enrty.getKey() + "," + "Value=" + enrty.getValue());
		}
		// 得到map的键对应的值
		String msgType = map.get("MsgType");
		String toUserName = map.get("ToUserName");
		String fromUserName = map.get("FromUserName");

		String picUrl = map.get("PicUrl"); // 图片属性
		String mediaId = map.get("MediaId"); // 图片属性
		// 作出相应！（给用户返回的信息！）
		String msg = "";
		if (msgType.equals("event")) {//
			String eventType = map.get("Event");
			if ("subscribe".equals(eventType)) {// 关注公众号时做出的反应
				System.out.println("---------关注公众号时-------");
				msg = MessageUtils.initText(toUserName, fromUserName, "您好！欢迎加入公众号！");
				// 关注过后得到用户信息
				String openid = map.get("FromUserName");
				System.out.println(openid);//openid就是fromUserName
				// 获取access_token
				String access_token = GetAccess_TokenUtils.getAccess_Token();
				String location = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_token
						+ "&openid=" + openid + "&lang=zh_CN";
				HttpGet get = new HttpGet(location);
				// 使用HttpClient发送get请求，获得返回结果HttpResponse
				HttpClient http = new DefaultHttpClient();
				HttpResponse response = http.execute(get);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					String result = EntityUtils.toString(entity);
					System.out.println("用户信息："+result);
				}
			}
		} else if ("text".equals(msgType)) {
			String content = "欢迎您的加入！";
			System.out.println("--------用户回复消息时-------");
			msg = MessageUtils.initText(toUserName, fromUserName, "您好，" + content);
		} else if ("image".equals(msgType)) {
			// 如何直接返回用户发送的照片！
			msg = MessageUtils.initphoto(toUserName, fromUserName, picUrl, mediaId);
		}
		System.out.println(msg);

		return msg;// 返回xml!
	}

	/**
	 * 自定义菜单栏
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@GetMapping("set")
	@ResponseBody
	public void personMenu() throws ClientProtocolException, IOException {
		String access_token = GetAccess_TokenUtils.getAccess_Token();
		String location = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + access_token;
		HttpPost post = new HttpPost(location);
		String json = CreateJsonUtils.CreatJson().toString();
		// System.out.println(json);
		// 发送json数据
		StringEntity myEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
		post.setEntity(myEntity);
		// 响应
		HttpClient http = new DefaultHttpClient();
		HttpResponse response = http.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			// 输出响应结果！
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity);
			System.out.println(result);
		}
	}

//	@GetMapping("weixin/getUser")
//	@ResponseBody
//	public String getUser(HttpServletRequest request) throws IOException, DocumentException {
//		// 用户得到openID
//		Map<String, String> map = MessageUtils.xmlToMap(request);
//		String openid = map.get("FromUserName");
//		System.out.println(openid);
//		// 获取access_token
//		String access_token = GetAccess_TokenUtils.getAccess_Token();
//		String location = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_token + "&openid="
//				+ openid + "&lang=zh_CN";
//		HttpGet get = new HttpGet(location);
//		// 使用HttpClient发送get请求，获得返回结果HttpResponse
//		HttpClient http = new DefaultHttpClient();
//		HttpResponse response = http.execute(get);
//		if (response.getStatusLine().getStatusCode() == 200) {
//			HttpEntity entity = response.getEntity();
//			String result = EntityUtils.toString(entity);
//			System.out.println(result);
//			return result;
//		}
//		return "";
//	}

}

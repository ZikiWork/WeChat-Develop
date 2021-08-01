package com.springboot.weixin.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.springboot.weixin.entity.PhotoMessage;
import com.springboot.weixin.entity.TextMessage;
import com.thoughtworks.xstream.XStream;

public class MessageUtils {
	/**
	 * 将请求中（用户发的信息）的xml信息转换为Map类型的数据
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);
		Element root = doc.getRootElement();
		List<Element> list = root.elements();
		for (Element e : list) {
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}

	public static String textMessageToXml(TextMessage textMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	public static String photoMessageToXml(PhotoMessage photoMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml", photoMessage.getClass());
		return xstream.toXML(photoMessage);
	}

	/**
	 * 组装文本消息为xml
	 * 
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String initText(String toUserName, String fromUserName, String content) {
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType("text");// 文本!对应文档中的参数！
		text.setCreateTime(new Date().getTime());
		text.setContent(content);
		return textMessageToXml(text);
	}

	/**
	 * 组装图片消息为xml（未解决）
	 * 
	 * @param toUserName
	 * @param fromUserName
	 * @param picUrl
	 * @return
	 */
	public static String initphoto(String toUserName, String fromUserName, String picUrl, String mediaId) {
		PhotoMessage photo = new PhotoMessage();
		photo.setFromUserName(toUserName);
		photo.setToUserName(fromUserName);
		photo.setMsgType("image");// 对应文档中的参数！（image）
		photo.setCreateTime(new Date().getTime());
		photo.setPicUrl(picUrl);
		photo.setMediaId(mediaId);
		return photoMessageToXml(photo);
	}
}

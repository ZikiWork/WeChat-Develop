package com.springboot.weixin.entity;

/**
 * 继承基础消息，再添加所需消息！
 * @author Administrator
 *
 */
public class TextMessage extends BassMessage {

	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
	
}

package com.springboot.weixin.entity;

/**
 * 创建点击型菜单事件
 * 
 * @author Administrator
 *
 */
public class ClickButton {
	private String type;
	private String name;
	private String key;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}

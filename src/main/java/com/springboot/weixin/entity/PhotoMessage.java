package com.springboot.weixin.entity;

/**
 * 继承基础信息，在加上所需的photo 属性！
 * @author Administrator
 *
 */
public class PhotoMessage extends BassMessage {
	private String PicUrl;

	private String MediaId;
	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
}

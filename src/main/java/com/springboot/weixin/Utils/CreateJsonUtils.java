package com.springboot.weixin.Utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springboot.weixin.entity.ClickButton;
import com.springboot.weixin.entity.ViewButton;

public class CreateJsonUtils {

	public static JSONObject CreatJson() {
		ClickButton cbt = new ClickButton();
		cbt.setKey("image");
		cbt.setName("回复图片");
		cbt.setType("click");

		ViewButton vbt = new ViewButton();
		vbt.setUrl("http://www.baidu.com");
		vbt.setName("搜索");
		vbt.setType("view");

		JSONArray button = new JSONArray();
		button.add(vbt);
		button.add(cbt);

		JSONObject menujson = new JSONObject();
		menujson.put("button", button);
		System.out.println(menujson);
		return menujson;
	}
}

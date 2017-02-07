package com.sd.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.json.JSONObject;

public class decoratedJSON {
//	private JSONArray jsonArray;
	private JSONObject jsonObject;

	public decoratedJSON(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
//
//	public decoratedJSON(JSONArray jsonArray) {
//		this.jsonArray = jsonArray;
//	}

//	public JSONArray add(Object obj) {
//		if (obj == null) {
//			return new JSONArray();
//		}
//		jsonArray.add(obj);
//		return jsonArray;
//
//	}

	public JSONObject put(String key, Object obj) {
		if (obj == null) {
			obj = "";
		} else if (obj instanceof Date) {
			obj = new SimpleDateFormat("yyyy-MM-dd").format((Date) obj);
		}
		jsonObject.put(key, obj);
		return jsonObject;
	}

	public JSONObject getReJsonOBJ() {
		return jsonObject;

	}

//	public JSONArray getReJsonARR() {
//		return jsonArray;
//
//	}


}

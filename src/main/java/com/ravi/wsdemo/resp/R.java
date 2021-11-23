package com.ravi.wsdemo.resp;


import java.util.HashMap;
import java.util.Map;

public class R extends HashMap<String, Object> {

	public R(int code, String msg) {
		put("code", code);
		put("msg", msg);
	}

	public R(){
		put("code", 200);
		put("msg", "success");
	}

	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	// 静态工厂方法
	public static R ok() {
		return new R();
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}

	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}

	public static R error(int code, String msg) {
		R r = new R();
		r.put("msg", msg);
		r.put("code", code);
		return r;
	}

	public static R error(String msg) {
		return error(5000, msg);
	}

	public static R error() {
		return error(5000, "未知异常！");
	}

}

package com.single.rxjava_retrofit30_okhttp.bean;

import java.util.List;

/**
 * @Description:接收数据的集合
 * @author：GaomingShuo
 * @date：${DATA} 10:50
 */
public class ResultList<T> {
	private String code; // 响应码
	private String msg; // 响应码描述
	private List<T> result;// 具体的业务有不同的返回参数
	private long time;// 响应时间z

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}


	@Override
	public String toString() {
		return "ResultList{" +
				"code='" + code + '\'' +
				", msg='" + msg + '\'' +
				", result=" + result +
				", time=" + time +
				'}';
	}
}

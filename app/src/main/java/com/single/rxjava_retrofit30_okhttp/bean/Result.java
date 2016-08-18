package com.single.rxjava_retrofit30_okhttp.bean;

/**
 * @Description:接收数据的单一
 * @author：GaomingShuo
 * @date：${DATA} 10:56
 */
public class Result<T> {
	private String status; // 响应码
	private String desc; // 响应码描述
	private T detail;// 具体的业务有不同的返回参数

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public T getDetail() {
		return detail;
	}

	public void setDetail(T detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		return "ResultG{" +
				"status='" + status + '\'' +
				", desc='" + desc + '\'' +
				", detail=" + detail +
				'}';
	}

}

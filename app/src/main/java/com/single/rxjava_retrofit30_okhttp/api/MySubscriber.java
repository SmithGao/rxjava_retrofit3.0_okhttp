package com.single.rxjava_retrofit30_okhttp.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.single.rxjava_retrofit30_okhttp.bean.Result;
import com.single.rxjava_retrofit30_okhttp.util.LogUtil;

import rx.Subscriber;

/**
 * 作者：Donn on 16/5/20 14:18
 * 邮箱：xjs250@163.com
 */
public abstract class MySubscriber<T> extends Subscriber<T> {
	String TAG = MySubscriber.class.getName();

	@Override
	public void onStart() {
		onMyStart();
	}

	@Override
	public void onCompleted() {
		onMyCompleted();
	}


	@Override
	public void onError(Throwable errorMsg) {
		String stringErr = errorMsg.getLocalizedMessage();
		//TODO 需要完善异常的判断
		if (stringErr.contains("UnknownHostException")) {
			onMyError("无法连接服务器，请检查网络是否正常");
		} else {
			onMyError(stringErr);
		}

	}

	@Override
	public void onNext(T t) {
		if (Result.class.isInstance(t)) {
			Result result = (Result) t;
			if (!result.getStatus().equals("000000")) {
				onMyError(result.getDesc());
			} else {
				if (null == result.getDetail() && "".equals(result.getDetail())) {
					onMyError("返回数据为空");
					return;
				} else {
					try {
						String jsonStr = ApiManager.objectMapper.writeValueAsString(result.getDetail());
						onMyNext(jsonStr);
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}

				}
			}
		} else {
			//如果没返回数据没指向Result，打印数据。后期有返回特殊数据格式的时候可以单独解析jsonStr
			try {
				String jsonStr = ApiManager.objectMapper.writeValueAsString(t);
//                onMyNext(jsonStr);
				LogUtil.printE(TAG, "原始数据" + jsonStr);
			} catch (JsonProcessingException e) {
			}
		}
	}


	public void onMyStart(){};

	public abstract void onMyNext(String jsonStr);

	public abstract void onMyError(String msg);

	public abstract void onMyCompleted();


}

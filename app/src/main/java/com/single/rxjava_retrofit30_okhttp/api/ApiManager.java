package com.single.rxjava_retrofit30_okhttp.api;

import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.single.rxjava_retrofit30_okhttp.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/*
   rxjava+Retrofit+okhttp3 之间串联
 */
public class ApiManager {
	public static ObjectMapper objectMapper = null;
	private static HashMap<String, String> map;
	//新闻
	private static final String SERVER_PRODUCT = "http://api.1-blog.com";

	private static final String TAG = "SingleTask";

	// OkHttp3 的监听
	private static class LogInterceptor implements Interceptor {
		@Override
		public okhttp3.Response intercept(Chain chain) throws IOException {
			Request request = chain.request();
			Log.e(TAG, "okhttp3:" + request.toString());//输出请求前整个url
			long t1 = System.nanoTime();
			okhttp3.Response response = chain.proceed(chain.request());
			long t2 = System.nanoTime();
//			Log.v(TAG,response.request().url()+response.headers());//输出一个请求的网络信息
			okhttp3.MediaType mediaType = response.body().contentType();
			String content = response.body().string();
			Log.e(TAG, "response body:" + content);//输出返回信息
			return response.newBuilder()
					.body(okhttp3.ResponseBody.create(mediaType, content))
					.build();
		}
	}

	//初始化OkHttp3的客户端
	private static OkHttpClient client = new OkHttpClient.Builder()
			.addInterceptor(new LogInterceptor())
			.cache(new Cache(new File("C://okhttp"), 10 * 1024 * 102))//缓存
			.retryOnConnectionFailure(true)
//			.connectTimeout(3, TimeUnit.SECONDS)
//			.writeTimeout(5, TimeUnit.SECONDS)
//			.readTimeout(5, TimeUnit.SECONDS)
			.build();


	//初始化Retrofit
	private static final Retrofit sRetrofit = new Retrofit.Builder()
			.baseUrl(SERVER_PRODUCT)
			.client(client)
			.addConverterFactory(JacksonConverterFactory.create())//加入json解析
			.addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 使用RxJava作为回调适配器
			.build();


	public static ApiManagerService apiManager = sRetrofit.create(ApiManagerService.class);


	/**
	 * 设置公共参数
	 */
	public static Map<String, String> getBasicMap() {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
			objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		}
		if (map == null) {
			map = new HashMap<>();
		} else {
			map.clear();
		}
		//打印MAP
		Set<Map.Entry<String, String>> entrySet = map.entrySet();
		for (Map.Entry<String, String> entry : entrySet) {
			LogUtil.printI(LogUtil.APP_TAG, entry.getKey() + "//" + entry.getValue() + "");
		}
		return map;
	}

}

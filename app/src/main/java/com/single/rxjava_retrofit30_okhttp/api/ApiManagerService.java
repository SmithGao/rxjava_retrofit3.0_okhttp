package com.single.rxjava_retrofit30_okhttp.api;

import com.single.rxjava_retrofit30_okhttp.bean.Result;

import java.util.Map;

import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @className：ApiManagerService
 */
public interface ApiManagerService {

	@POST("/biz/bizserver/news/list.do")
	Observable<Result> getReslut(@QueryMap Map<String, String> option);
}

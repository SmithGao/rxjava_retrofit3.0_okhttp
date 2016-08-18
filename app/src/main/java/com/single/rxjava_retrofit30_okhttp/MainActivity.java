package com.single.rxjava_retrofit30_okhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.single.rxjava_retrofit30_okhttp.api.ApiManager;
import com.single.rxjava_retrofit30_okhttp.api.MySubscriber;
import com.single.rxjava_retrofit30_okhttp.bean.Result;
import com.single.rxjava_retrofit30_okhttp.util.LogUtil;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

	@Bind(R.id.btn)
	public Button button;

	@Bind(R.id.data)
	TextView data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
	}


	//--------------------------------------------------------------------------------------------------

	@OnClick(R.id.btn)
	void more() {
		Map<String, String> result = ApiManager.getBasicMap();
		result.put("size", "3");
		ApiManager.apiManager.getReslut(result)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new MySubscriber<Result>() {
					@Override
					public void onMyNext(String jsonStr) {
						LogUtil.defLog(jsonStr);
						data.setText(jsonStr);
					}

					@Override
					public void onMyError(String msg) {

					}

					@Override
					public void onMyCompleted() {

					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}

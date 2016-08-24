# rxjava+retrofit+okhttp3
## 一：项目简介
重所周知 当下最流行的网络请求的框架非rxjava+retrofit+okhttp3三合一了
但是在网络上总是找不到一个比较全面的介绍 
于是呼我自己研究了一套网络请求的发开框架 目前已经写入我开发的项目当中
目前还在学习的小伙伴们可以学习一下
- (此篇文章主要针对我的demo 很多细节的东西讲的不是很到位 但是我会把我在网上看的比较好的文章贴上去 让大家详细的了解)

### Rxjava
简洁：对于rxjava的理解 我个人认为就是一个异步线程一个基于事件处理的库
让人们可以更简洁的处理一个事件的逻辑关系 简单性 可读性 重复性。再往深处的理解大家也可以认为他是扩展了开发者模式演变过来的
##### 举例：观察者(大s)  被观察者(小s) 只有当小s发生了事件 或者说小s处罚了某个事件 大s 才会对小s 进行相应的处理 否则 大s 会一直处于一个等待的状态
当然我说的可能会比较粗糙，我给大家介绍一个对Rxjava理解比较好的作者 相信你们看了他的讲解 会比我这里更加的明白和理解。[点击－抛物线](http://gank.io/post/560e15be2dca930e00da1083)

### Retrofit
简洁：Retrofit与okhttp共同出自于Square公司，retrofit就是对okhttp做了一层封装。把网络请求都交给给了Okhttp，我们只需要通过简单的配置就能使用retrofit来进行网络请求。

Retrofit与Okhttp不同的是，Retrofit需要定义一个接口，用来返回我们的Call对象

```
public interface RequestServes {
    @POST("mobileLogin/submit.html")
    Call<String> getReslut(@Query("loginname") String loginname);
}
```
或

```
public interface ApiManagerService {

	@POST("/biz/bizserver/news/list.do")
	Observable<Result> getReslut(@QueryMap Map<String, String> option);
}
```
我先说一下Result类，这是我自己demo中使用的实体类接数据的，我是结合了rxjava的Subscriber(观察者)来封装的后面的文段我会在次讲解 这里给大家留个悬念<img src="emoji/smile" width="18"/><img src="emoji/santa" width="18"/>
#####注 ：次处以在ApiManagerService类中声明

显而易见@POST，@GET的是请求方式，参数注解有@PATH和@Query，@QueryMap等。
@Query @QueryMap 参数依旧遵循了http协议 以键值对的形式进行请求。

##### 到此接口我们就定义完成了需要来定义Retrofit对象来进行请求了
俺就不再废话了 直接贴上代码 让大家伙看个明白地


```
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
```

###### 这是OkHttp3自带的监听可以输出整个url 还可以打印请求数据的时间 让开发者很清楚的了解请求的耗时操作流程－真心的良心制作<img src="emoji/dog" width="18"/>
```
//初始化OkHttp3的初始化
	private static OkHttpClient client = new OkHttpClient.Builder()
			.addInterceptor(new LogInterceptor())
			.cache(new Cache(new File("C://okhttp"), 10 * 1024 * 102))//缓存
			.retryOnConnectionFailure(true)
//			.connectTimeout(3, TimeUnit.SECONDS)
//			.writeTimeout(5, TimeUnit.SECONDS)
//			.readTimeout(5, TimeUnit.SECONDS)
			.build();
```
这是化OkHttp3的初始化
###### 1:添加OKhttp3的监听事件
###### 2:清理数据的缓存
###### 3:在链接失败后重新链接
###### 4:链接，读，写的时间 可以自己设置 当然不写也无所谓啦<img src="emoji/stuck_out_tongue_winking_eye" width="18"/>
###### 5:最后 build()
```

//初始化Retrofit
	private static final Retrofit sRetrofit = new Retrofit.Builder()
			.baseUrl("http://api.1-blog.com")
			.client(client)
			.addConverterFactory(JacksonConverterFactory.create())//加入jackjson解析
			.addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 使用RxJava作为回调适配器
			.build();
```
初始化Retrofit 
###### 1:添加请求头
###### 2:添加okhttp的对象(前面也说了－retrofit就是对okhttp做了一层封装)
###### 3: 重点来 重点来 重点来重要的事情要说三遍
##### 解析

```
Gson: com.squareup.retrofit2:converter-gson
Jackson: com.squareup.retrofit2:converter-jackson
Moshi: com.squareup.retrofit2:converter-moshi
Protobuf: com.squareup.retrofit2:converter-protobuf
Wire: com.squareup.retrofit2:converter-wire
Simple XML: com.squareup.retrofit2:converter-simplexml
Scalars (primitives, boxed, and String): com.squareup.retrofit2:converter-scalars
```
总有一款你需要的解析形式 附赠官网retrofit 官网地址：http://square.github.io/retrofit/  启动你的翻译器慢慢看吧！(英语大神除外哈 <img src="emoji/cow" width="18"/>)
大家可以看到 我添加的是jackjson解析 这是我demo中所需要的 后续一起讲解。
在此介绍一片文章 [点击一下吧](http://blog.csdn.net/u012301841/article/details/49685677)

---

### 大集合
```
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
```
朋友 看到这请你告诉我 你是不是一脸的懵b 别着急 前面介绍了半天 现在正主正在一点点的出来了
咱们一步步的走我来给你解释一下<img src="emoji/smile" width="18"/>

 ###### 1  :Map集合ApiManager.getBasicMap() 自己封装了一下 可以将以后项目中的公共参数放进去 
######  例如：

```
/**
	 * 设置公共参数
	 */
	public static Map<String, String> getBasicMap() {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
			objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
			objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

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
```
其中objectMapper 即是jackjson的解析设置 
###### a）objectMapper.enable 一个简单的映射过程 确保数据能够全部请求过来
###### b）objectMapper.configure 设置当返回的参数为null时可自动执行序列化操作 当然 记得把你的实体类写个空的构造方法。
以上两个配置我个人介意还是加上 省的大家走很多的弯路 (我不哭<img src="emoji/sob" width="18"/>)

---

###### 2:ApiManager.apiManager.getReslut(result)
在此调用网络请求

```
public static ApiManagerService apiManager = sRetrofit.create(ApiManagerService.class);
```
在开始介绍的由Retrofit定义的接口类 各种的封装啊。。。。
OK
###### 3:接下来正主来了 rxjava重出江湖

```
.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
```
设置流文件处理，设置执行在主线程。
###### 4:.subscribe(new MySubscriber<Result>()  What is fuck?
你猜对了 我又给封装了 

```
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
			if (!result.getStatus().equals("success")) {
				onMyError(result.getInfo());
			} else {
				if (null == result.getData() && "".equals(result.getData())) {
					onMyError("返回数据为空");
					return;
				} else {
					try {
						String jsonStr = ApiManager.objectMapper.writeValueAsString(result.getData());
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


	public abstract void onMyStart();

	public abstract void onMyNext(String jsonStr);

	public abstract void onMyError(String msg);

	public abstract void onMyCompleted();
```
虽然这里类在代码里有 但是我还是选择了全部贴出来 讲解开始：
###### 1:创建一个抽象类MySubscriber<T> 继承Subscriber 人有会问 为啥里面带范型 T 呢
范型的定义：泛型，即“参数化类型”。一提到参数，最熟悉的就是定义方法时有形参，然后调用此方法时传递实参。那么参数化类型怎么理解呢？顾名思义，就是将类型由原来的具体的类型参数化，类似于方法中的变量参数，此时类型也定义成参数形式（可以称之为类型形参），然后在使用/调用时传入具体的类型（类型实参）

所以在接收参数的时候 我可以任意的将参数实例化(避免了参数写死) 说大白话就是：我在项目a中的根节点是｛1，2，3｝那我在项目b中的根节点可能就要变成{a,b,c,d} 

#####  值得注意的是在你的实体类Result中 也要使用范型来接收数据即Result < T > 否则无效，甚至报错。到时候大家可以往底层的代码看看 其实Subscriber是实现了观察者Observer的方法的 在这里我不再深多解释－双手将链接奉上[点击－抛物线](http://gank.io/post/560e15be2dca930e00da1083) 

好了 当我的MySubscriber< T > 继承Subscriber 我同样也是实现了它内部的方法 

```
onStart()
onCompleted()
onError(Throwable errorMsg)
onNext(T t)
```
同时 我自己也写了四个抽象的方法来作为整个抽象类的返回值
在此处 onNext(T t) 承载了返回的数据 到时大家可以根据自己(公司的)项目来更改 ,方法中 唯一必须要写的就是
 
```
String jsonStr = ApiManager.objectMapper.writeValueAsString(result.getData());
```
此方法是jackjson中 将返回的json 转化成字符串的形式返回出去，在我的MainActivity中 我又自己写了个ParseJsonUtil类 来处理返回的字符串(这样很大问题解决了重复性)  可转化集合 或者单个的实体 具体的可在log中打印出来 哦 对了 我的GoodVideoBean只是个样品 没有实例的数据返回 大家可以尝试写一下 假如在又不明白的可以加我的qq 2215719882 来询问我 

#### 最后 欢迎 start －－屌丝程序员一枚 

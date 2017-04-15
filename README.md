# Volley
* 此库为精简之后的volley！
* 本来是打算精简原版的Volley，试过之后发现如果要完全移除HttpClient，有太多东西需要重写，
  半天之后宣布放弃，考虑后决定要站在巨人的肩膀上，即此库是在开源实验室张涛的RxVolley的基础
  上移除掉RxJava的支持，用的时候自己包装入RxJava，虽然没有多少代码，但是我自己的项目当中
  会出现很多意想不到的错误，所以移除。然后是下载File功能也移除，原因是对我个人显得有些多余，
  下载功能表现不佳。之后是移除了RxBus，原因是项目中已有RxBus的实现，自带的也不符合个人使用
  习惯。做完这些之后包大概小了23K的大小，虽然不多，但是也许就会有人跟我一样需要这样的精简！
  另外由于上传到maven库错误，暂时就没这个链接，如果后续有需要可以issue我，我也好发挥自己开
  源精神，搞定上传的问题供大家使用！
* 由于移除了Rxjava的支持，叫RxVolley好像不合适，so使用除了RxVolley改为VolleyGo外，其他大部分相同
* 本人习惯链式调用，所以改成了链式，源码也不复杂，大家一看就能懂！
* use：
```
	VolleyGo.post()
	        .url(url)
	        .params(params)
	        .onSuccessWithString(new HttpCallback.SuccessWithString() {
	            @Override public void onSuccess(String t) {
	              System.out.println("t = " + t);
	            }
	        })
	        .doTask();
```
* 暂时不能用kymjs的okhttp扩展，如果大家有需要会修改以完美支持
* 图片加载的扩展也删了，我喜欢用专门的加载框架如Glide，要是大家有需要我添加回去
* 暂时就只有这么多了
* 另外这个库是精简了kymjs张涛的RxVolley的作品，本无意侵犯版权，如果侵犯了权益，请告知，我会马上删除，谢谢！

email：gt1254094162@163.com

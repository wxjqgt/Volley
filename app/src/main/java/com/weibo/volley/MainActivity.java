package com.weibo.volley;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.gson.Gson;
import com.weibo.library.VolleyGo;
import com.weibo.library.client.HttpCallback;
import com.weibo.library.client.HttpParams;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.weibo.volley.FaxianApi.url;

public class MainActivity extends AppCompatActivity {
  //source=android&lon=&lat=&format=json&
  Gson gson = new Gson();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    /*Retrofit retrofit =
        new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .build();
    Map<String, String> params1 = new HashMap<>();
    params1.put("source", "android");
    params1.put("lon", "");
    params1.put("lat", "");
    params1.put("format", "json");

    final long c1 = System.currentTimeMillis();
    retrofit.create(FaxainApi.class)
        .getData(params1)
        .subscribeOn(Schedulers.io())
        .subscribe(new Consumer<Faxian>() {
          @Override public void accept(Faxian faxian) throws Exception {
            System.out.println("faxainR = " + faxian.toString());
            System.out.println("timeR = " + (System.currentTimeMillis() - c1));
          }
        });*/

    HttpParams params = new HttpParams();
    params.put("source", "android");
    params.put("lon", "");
    params.put("lat", "");
    params.put("format", "json");
    VolleyGo.post()
        .url(url + "v6/faxian_new2.php?format=json")
        .params(params)
        .callback(new HttpCallback() {
          @Override public void onSuccess(String t) {
            Observable.just(gson.fromJson(t, Faxian.class))
                .flatMap(new Function<Faxian, ObservableSource<Faxian.FaxianListBean>>() {
                  @Override public ObservableSource<Faxian.FaxianListBean> apply(Faxian faxian)
                      throws Exception {
                    return Observable.fromIterable(faxian.getFaxian_list());
                  }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Faxian.FaxianListBean>() {
                  @Override public void accept(Faxian.FaxianListBean faxianListBean)
                      throws Exception {
                    System.out.println(faxianListBean.getTitle());
                  }
                });
          }
        })
        .doTask();
  }
}


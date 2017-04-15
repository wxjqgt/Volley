package com.weibo.volley;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.gson.Gson;
import com.weibo.library.Volley;
import com.weibo.library.client.HttpCallback;
import com.weibo.library.client.HttpParams;

public class MainActivity extends AppCompatActivity {

  String url = "http://api.meishi.cc/v6/faxian_new2.php?format=json";
  //source=android&lon=&lat=&format=json&
  Gson gson = new Gson();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    HttpParams params = new HttpParams();
    params.put("source", "android");
    params.put("lon", "");
    params.put("lat", "");
    params.put("format", "json");
    final long c = System.currentTimeMillis();
    Volley.post()
        .url(url)
        .params(params)
        .onSuccessWithString(new HttpCallback.SuccessWithString() {
            @Override public void onSuccess(String t) {
              System.out.println("t = " + t);
            }
        })
        .doTask();
  }
}

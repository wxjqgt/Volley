package com.weibo.volley;

import io.reactivex.Observable;
import java.util.Map;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by weibo on 17-4-15.
 */

public interface FaxianApi {
  String url = "http://api.meishi.cc/";

  @POST("v6/faxian_new2.php?format=json") @Multipart Observable<Faxian> getData(
      @PartMap Map<String, String> params);
}

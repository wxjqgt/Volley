/*
 * Copyright (c) 2014, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.weibo.library.client;

import com.weibo.library.http.VolleyError;
import java.util.Map;

/**
 * Http请求回调类<br>
 */
public class HttpCallback {

  public interface PreStart {
    /**
     * 请求开始之前回调
     */
    void onPreStart();
  }

  public interface PreHttp {
    /**
     * 发起Http之前调用(只要是内存缓存中没有就会被调用)
     */
    void onPreHttp();
  }

  public interface SuccessInAsync {
    /**
     * 注意：本方法将在异步调用。
     * Http异步请求成功时在异步回调,并且仅当本方法执行完成才会继续调用onSuccess()
     *
     * @param t 返回的信息
     */
    void onSuccessInAsync(byte[] t);
  }

  public interface SuccessWithString {
    /**
     * Http请求成功时回调
     *
     * @param t HttpRequest返回信息
     */
    void onSuccess(String t);
  }

  public interface SuccessWithByte {
    /**
     * Http请求成功时回调
     *
     * @param headers HttpRespond头
     * @param t HttpRequest返回信息
     */
    //    onSuccess(new String(t));
    void onSuccess(Map<String, String> headers, byte[] t);
  }

  public interface FailureWithMsg {
    /**
     * Http请求失败时回调
     *
     * @param errorNo 错误码
     * @param strMsg 错误原因
     */
    void onFailure(int errorNo, String strMsg);
  }

  public interface FailureWithError {
    /**
     * Http请求失败时回调
     * 仅Http网络请求中有效
     */
    void onFailure(VolleyError error);
  }

  public interface Finish {
    /**
     * Http请求结束后回调
     */
    void onFinish();
  }
}

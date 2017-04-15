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
package com.weibo.library;

import android.icu.util.TimeUnit;
import android.text.TextUtils;
import com.weibo.library.client.FormRequest;
import com.weibo.library.client.HttpCallback;
import com.weibo.library.client.HttpParams;
import com.weibo.library.client.JsonRequest;
import com.weibo.library.client.ProgressListener;
import com.weibo.library.client.RequestConfig;
import com.weibo.library.constant.ContentType;
import com.weibo.library.constant.Method;
import com.weibo.library.http.Request;
import com.weibo.library.http.RequestQueue;
import com.weibo.library.http.RetryPolicy;
import com.weibo.library.interf.ICache;
import com.weibo.library.util.FileUtils;
import java.io.File;

/**
 * 主入口
 *
 * @author weibo on 17/04/14.
 */
public class VolleyGo {

  private VolleyGo() {
  }

  public final static File CACHE_FOLDER = FileUtils.getExternalCacheDir("RxVolley");

  private static RequestQueue sRequestQueue;

  /**
   * 获取一个请求队列(单例)
   */
  public synchronized static RequestQueue getRequestQueue() {
    if (sRequestQueue == null) {
      sRequestQueue = RequestQueue.newRequestQueue(CACHE_FOLDER);
    }
    return sRequestQueue;
  }

  /**
   * 设置请求队列,必须在调用Core#getRequestQueue()之前设置
   *
   * @return 是否设置成功
   */
  public synchronized static boolean setRequestQueue(RequestQueue queue) {
    if (sRequestQueue == null) {
      sRequestQueue = queue;
      return true;
    } else {
      return false;
    }
  }

  /**
   * 构建器
   */
  public static class Builder {

    private HttpCallback.PreStart mPreStart;
    private HttpCallback.PreHttp mPreHttp;
    private HttpCallback.SuccessWithString mSuccessWithString;
    private HttpCallback.SuccessWithByte mSuccessWithByte;
    private HttpCallback.SuccessInAsync mSuccessInAsync;
    private HttpCallback.FailureWithMsg mFailureWithMsg;
    private HttpCallback.FailureWithError mFailureWithError;
    private HttpCallback.Finish mFinish;
    private HttpParams params;
    private ContentType contentType;
    private Request<?> request;
    private ProgressListener progressListener;
    private RequestConfig httpConfig = new RequestConfig();

    private void checkParmsNotNull() {
      if (this.params == null) {
        this.params = new HttpParams();
      }
    }

    /**
     * Http请求参数
     */
    public Builder params(HttpParams params) {
      this.params = params;
      return this;
    }

    public Builder addParam(String key, String value) {
      checkParmsNotNull();
      this.params.put(key, value);
      return this;
    }

    public Builder addParam(String key, int value) {
      checkParmsNotNull();
      this.params.put(key, value);
      return this;
    }


    public Builder addParam(String key, byte[] value) {
      checkParmsNotNull();
      this.params.put(key, value);
      return this;
    }

    public Builder addParam(String key, File value) {
      checkParmsNotNull();
      this.params.put(key, value);
      return this;
    }

    /**
     * 参数的类型:FORM表单,或 JSON内容传递
     */
    public Builder contentType(ContentType contentType) {
      this.contentType = contentType;
      return this;
    }

    /**
     * 请求回调,不需要可以为空
     */
    public Builder onPreStart(HttpCallback.PreStart callback) {
      if (callback == null) {
        throw new IllegalStateException("the PreStartcallback is null");
      }
      this.mPreStart = callback;
      return this;
    }

    public Builder onPreHttp(HttpCallback.PreHttp callback) {
      if (callback == null) {
        throw new IllegalStateException("the PreHttpcallback is null");
      }
      this.mPreHttp = callback;
      return this;
    }

    public Builder onSuccessWithString(HttpCallback.SuccessWithString callback) {
      if (callback == null) {
        throw new IllegalStateException("the SuccessWithStringcallback is null");
      }
      this.mSuccessWithString = callback;
      return this;
    }

    public Builder onSuccessWithByte(HttpCallback.SuccessWithByte callback) {
      if (callback == null) {
        throw new IllegalStateException("the SuccessWithBytecallback is null");
      }
      this.mSuccessWithByte = callback;
      return this;
    }

    public Builder onSuccessInAsync(HttpCallback.SuccessInAsync callback) {
      if (callback == null) {
        throw new IllegalStateException("the SuccessInAsynccallback is null");
      }
      this.mSuccessInAsync = callback;
      return this;
    }

    public Builder onFailureWithMsg(HttpCallback.FailureWithMsg callback) {
      if (callback == null) {
        throw new IllegalStateException("the FailureWithMsgcallback is null");
      }
      this.mFailureWithMsg = callback;
      return this;
    }

    public Builder onFailureWithError(HttpCallback.FailureWithError callback) {
      if (callback == null) {
        throw new IllegalStateException("the FailureWithErrorcallback is null");
      }
      this.mFailureWithError = callback;
      return this;
    }

    public Builder onFinish(HttpCallback.Finish callback) {
      if (callback == null) {
        throw new IllegalStateException("the Finishcallback is null");
      }
      this.mFinish = callback;
      return this;
    }

    /**
     * HttpRequest
     */
    public Builder setRequest(Request<?> request) {
      this.request = request;
      return this;
    }

    /**
     * 每个request可以设置一个标志,用于在cancel()时找到
     */
    public Builder setTag(Object tag) {
      this.httpConfig.mTag = tag;
      return this;
    }

    /**
     * HttpRequest的配置器
     */
    public Builder httpConfig(RequestConfig httpConfig) {
      this.httpConfig = httpConfig;
      return this;
    }

    /**
     * 请求超时时间,如果不设置则使用重连策略的超时时间,默认3000ms
     */
    public Builder timeout(int timeout) {
      this.httpConfig.mTimeout = timeout;
      return this;
    }

    /**
     * 上传进度回调
     *
     * @param listener 进度监听器
     */
    public Builder progressListener(ProgressListener listener) {
      this.progressListener = listener;
      return this;
    }

    /**
     * 为了更真实的模拟网络,如果读取缓存,延迟一段时间再返回缓存内容
     */
    public Builder delayTime(int delayTime) {
      this.httpConfig.mDelayTime = delayTime;
      return this;
    }

    /**
     * 缓存有效时间,单位分钟
     */
    public Builder cacheTime(int cacheTime) {
      this.httpConfig.mCacheTime = cacheTime;
      return this;
    }

    /**
     * 缓存有效时间,单位分钟
     */
    public Builder cacheTime(int cacheTime, TimeUnit timeUnit) {
      this.httpConfig.mCacheTime = cacheTime;
      return this;
    }

    /**
     * 是否使用服务器控制的缓存有效期(如果使用服务器端的,则无视#cacheTime())
     */
    public Builder useServerControl(boolean useServerControl) {
      this.httpConfig.mUseServerControl = useServerControl;
      return this;
    }

    /**
     * 查看RequestConfig$Method
     * GET/POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
     */
    public Builder httpMethod(Method httpMethod) {
      this.httpConfig.mMethod = httpMethod;
      if (httpMethod == Method.POST) {
        this.httpConfig.mShouldCache = false;
      }
      return this;
    }

    /**
     * 是否启用缓存
     */
    public Builder shouldCache(boolean shouldCache) {
      this.httpConfig.mShouldCache = shouldCache;
      return this;
    }

    /**
     * 网络请求接口url
     */
    public Builder url(String url) {
      this.httpConfig.mUrl = url;
      return this;
    }

    /**
     * 重连策略,不传则使用默认重连策略
     */
    public Builder retryPolicy(RetryPolicy retryPolicy) {
      this.httpConfig.mRetryPolicy = retryPolicy;
      return this;
    }

    /**
     * 编码,默认UTF-8
     */
    public Builder encoding(String encoding) {
      this.httpConfig.mEncoding = encoding;
      return this;
    }

    private Builder build() {
      if (request == null) {
        if (params == null) {
          params = new HttpParams();
        } else {
          if (httpConfig.mMethod == Method.GET) {
            httpConfig.mUrl += params.getUrlParams();
          }
        }

        if (httpConfig.mShouldCache == null) {
          //默认情况只对get请求做缓存
          if (httpConfig.mMethod == Method.GET) {
            httpConfig.mShouldCache = Boolean.TRUE;
          } else {
            httpConfig.mShouldCache = Boolean.FALSE;
          }
        }

        if (contentType == ContentType.JSON) {
          request =
              new JsonRequest(httpConfig, params, mPreHttp, mSuccessWithString, mSuccessWithByte,
                  mSuccessInAsync, mFailureWithMsg, mFailureWithError, mFinish);
        } else {
          request =
              new FormRequest(httpConfig, params, mPreHttp, mSuccessWithString, mSuccessWithByte,
                  mSuccessInAsync, mFailureWithMsg, mFailureWithError, mFinish);
        }

        request.setTag(httpConfig.mTag);
        request.setOnProgressListener(progressListener);

        if (TextUtils.isEmpty(httpConfig.mUrl)) {
          throw new RuntimeException("Request url is empty");
        }
      }
      if (mPreStart != null) {
        mPreStart.onPreStart();
      }
      return this;
    }

    /**
     * 执行请求任务
     */
    public void doTask() {
      build();
      getRequestQueue().add(request);
    }
  }

  public static Builder get() {
    return new Builder().httpMethod(Method.GET);
  }

  public static Builder post() {
    return new Builder().httpMethod(Method.POST);
  }

  /**
   * 获取到在本地的二进制缓存
   *
   * @param url 缓存的key
   * @return 缓存内容
   */
  public static byte[] getCache(String url) {
    ICache cache = getRequestQueue().getCache();
    if (cache != null) {
      ICache.Entry entry = cache.get(url);
      if (entry != null) {
        return entry.data;
      }
    }
    return new byte[0];
  }
}

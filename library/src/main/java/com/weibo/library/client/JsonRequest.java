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

import com.weibo.library.VolleyGo;
import com.weibo.library.http.HttpHeaderParser;
import com.weibo.library.http.NetworkResponse;
import com.weibo.library.http.Request;
import com.weibo.library.http.Response;
import com.weibo.library.toolbox.HttpParamsEntry;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 用来发起application/json格式的请求的，我们平时所使用的是form表单提交的参数，
 * 而使用JsonRequest提交的是json参数。
 */
public class JsonRequest extends Request<byte[]> {

  private final String mRequestBody;
  private final HttpParams mParams;
  private HttpCallback.SuccessWithString mSuccessWithString;
  private HttpCallback.SuccessWithByte mSuccessWithByte;

  public JsonRequest(RequestConfig config, HttpParams params, HttpCallback.PreHttp preHttp,
      HttpCallback.SuccessWithString successWithString,
      HttpCallback.SuccessWithByte successWithByte, HttpCallback.SuccessInAsync successInAsync,
      HttpCallback.FailureWithMsg failureWithMsg, HttpCallback.FailureWithError failureWithError,
      HttpCallback.Finish finish) {
    super(config, preHttp, successInAsync, failureWithMsg, failureWithError, finish);
    mSuccessWithString = successWithString;
    mSuccessWithByte = successWithByte;

    mRequestBody = params.getJsonParams();
    mParams = params;
  }

  @Override public ArrayList<HttpParamsEntry> getHeaders() {
    return mParams.getHeaders();
  }

  @Override protected void deliverResponse(Map<String, String> headers, byte[] response) {
    if (mSuccessWithByte != null) {
      mSuccessWithByte.onSuccess(headers, response);
    }
    if (mSuccessWithString != null) {
      mSuccessWithString.onSuccess(new String(response));
    }
  }

  @Override public Response<byte[]> parseNetworkResponse(NetworkResponse response) {
    return Response.success(response.data, response.headers,
        HttpHeaderParser.parseCacheHeaders(getUseServerControl(), getCacheTime(), response));
  }

  @Override public String getBodyContentType() {
    return String.format("application/json; charset=%s", getConfig().mEncoding);
  }

  @Override public String getCacheKey() {
    if (getMethod() == VolleyGo.Method.POST) {
      return getUrl() + mParams.getJsonParams();
    } else {
      return getUrl();
    }
  }

  @Override public byte[] getBody() {
    try {
      return mRequestBody == null ? null : mRequestBody.getBytes(getConfig().mEncoding);
    } catch (UnsupportedEncodingException uee) {
      return null;
    }
  }

  @Override public Priority getPriority() {
    return Priority.IMMEDIATE;
  }
}

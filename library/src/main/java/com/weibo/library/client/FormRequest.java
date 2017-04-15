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
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * Form表单形式的Http请求
 */
public class FormRequest extends Request<byte[]> {

  private final HttpParams mParams;
  private HttpCallback.SuccessWithString mSuccessWithString;
  private HttpCallback.SuccessWithByte mSuccessWithByte;

  public FormRequest(RequestConfig config, HttpParams params, HttpCallback.PreHttp preHttp,
      HttpCallback.SuccessWithString successWithString,
      HttpCallback.SuccessWithByte successWithByte, HttpCallback.SuccessInAsync successInAsync,
      HttpCallback.FailureWithMsg failureWithMsg, HttpCallback.FailureWithError failureWithError,
      HttpCallback.Finish finish) {
    super(config, preHttp, successInAsync, failureWithMsg, failureWithError, finish);
    mSuccessWithString = successWithString;
    mSuccessWithByte = successWithByte;
    if (params == null) {
      params = new HttpParams();
    }
    this.mParams = params;
  }

  @Override public String getCacheKey() {
    if (getMethod() == VolleyGo.Method.POST) {
      return getUrl() + mParams.getUrlParams();
    } else {
      return getUrl();
    }
  }

  @Override public String getBodyContentType() {
    if (mParams.getContentType() != null) {
      return mParams.getContentType();
    } else {
      return super.getBodyContentType();
    }
  }

  @Override public ArrayList<HttpParamsEntry> getHeaders() {
    return mParams.getHeaders();
  }

  @Override public byte[] getBody() {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      if (mProgressListener != null) {
        mParams.writeTo(
            new CountingOutputStream(bos, mParams.getContentLength(), mProgressListener));
      } else {
        mParams.writeTo(bos);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bos.toByteArray();
  }

  @Override public Response<byte[]> parseNetworkResponse(NetworkResponse response) {
    return Response.success(response.data, response.headers,
        HttpHeaderParser.parseCacheHeaders(getUseServerControl(), getCacheTime(), response));
  }

  @Override protected void deliverResponse(Map<String, String> headers, final byte[] response) {
    if (mSuccessWithByte != null) {
      mSuccessWithByte.onSuccess(headers, response);
    }
    if (mSuccessWithString != null) {
      mSuccessWithString.onSuccess(new String(response));
    }
  }

  @Override public Priority getPriority() {
    return Priority.IMMEDIATE;
  }

  public static class CountingOutputStream extends FilterOutputStream {
    private final ProgressListener progListener;
    private long transferred;
    private long fileLength;

    public CountingOutputStream(final OutputStream out, long fileLength,
        final ProgressListener listener) {
      super(out);
      this.fileLength = fileLength;
      this.progListener = listener;
      this.transferred = 0;
    }

    public void write(int b) throws IOException {
      out.write(b);
      if (progListener != null) {
        this.transferred++;
        if ((transferred % 20 == 0) && (transferred <= fileLength)) {
          VolleyGo.getRequestQueue()
              .getDelivery()
              .postProgress(this.progListener, this.transferred, fileLength);
        }
      }
    }
  }
}

/*
Copyright 2015 Brajesh Kumar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.kumar.brajesh.couriertracker.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by brajesh.k on 07/07/15.
 */
public class VolleyRequest<T extends BaseResponse> extends Request<T> {
    private WeakReference<NetworkListener> mListenerReference;
    // gson reflection
    private final Class<T> mResponseClazz;
    private Gson mGson;
    private byte[] mParams;
    private Map<String, String> mHeaders;

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }


    public VolleyRequest(int method, String url, NetworkListener listener,
                         Class<T> clazz, byte[] params, Map<String, String> headers) {
        super(method, url, null);
        mListenerReference = new WeakReference<NetworkListener>(listener);
        mResponseClazz = clazz;
        mParams = params;
        mHeaders = headers;
        mGson = new Gson();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    mGson.fromJson(json, mResponseClazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if (mListenerReference != null && mListenerReference.get() != null) {
            NetworkListener listener = mListenerReference.get();
            listener.onSuccess(response);

        }
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        if (mListenerReference != null && mListenerReference.get() != null) {
            NetworkListener listener = mListenerReference.get();
            BaseError response = new BaseError();

            if (error.networkResponse != null) {
                response.mStatusCode = error.networkResponse.statusCode;
                response.mErrorMessage = new String(error.networkResponse.data);
            } else if (error instanceof NetworkError || error instanceof NoConnectionError) {
                response.mErrorMessage = "Please check the internet connection";
            } else if (error instanceof TimeoutError) {
                response.mErrorMessage = "Server is busy try again later";
            } else if (error instanceof AuthFailureError) {
                response.mErrorMessage = "Auth Failure";
            } else if (error instanceof ServerError) {
                response.mErrorMessage = "ServerError";
            } else if (error instanceof ParseError) {
                response.mErrorMessage = "Error in parsing server data";
            }

            listener.onError(response);
        }

    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mParams != null) {
            return mParams;
        }
        return super.getBody();
    }
}

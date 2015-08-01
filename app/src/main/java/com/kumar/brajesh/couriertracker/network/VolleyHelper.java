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

import android.content.Context;
import android.net.Uri;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kumar.brajesh.couriertracker.TrackerLog;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by brajesh.k on 07/07/15.
 */
public class VolleyHelper {

    public static <T extends BaseResponse> void
    executeRequest(Context context, String url, int method,
                   Map<String, String> headers, BaseRequest requestMessage, Class<T> clazz,
                   NetworkListener listener, Object tag, boolean shouldCache, RetryPolicy retryPolicy) {
        VolleyRequest<T> request = null;
        if (VolleyRequest.Method.GET == method) {
            request = new VolleyRequest<T>(method, getUrl(url, requestMessage), listener, clazz, null, headers);
        } else if (VolleyRequest.Method.POST == method) {
            request = new VolleyRequest<T>(method, url, listener, clazz, getPostBody(requestMessage), headers);
        } else {
            TrackerLog.v("%s", "Method not implemented");
        }
        request.setTag(tag);
        request.setShouldCache(true);
        request.setRetryPolicy(retryPolicy);
        VolleyFactory.getInstance(context).addToRequestQueue(request);
    }

    public static <T extends BaseResponse> void
    executeRequest(Context context, String url, int method,
                   Map<String, String> headers, BaseRequest requestMessage, Class<T> clazz,
                   NetworkListener listener, Object tag) {
        // default retry policy
        // 5 sec, 3 times
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(5000, 3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        executeRequest(context, url, method, headers, requestMessage, clazz, listener, tag, true, retryPolicy);
    }

    public static String getUrl(String baseUrl, final BaseRequest request) {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Uri.Builder builder = Uri.parse(baseUrl + "?").buildUpon();
        Gson gson = new Gson();
        String jsonString = gson.toJson(request);
        Map<String, String> urlParams = gson.fromJson(jsonString, type);
        for (Map.Entry<String, String> entry : urlParams.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        return builder.toString();

    }

    public static byte[] getPostBody(final BaseRequest request) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(request);

        return jsonString.getBytes();// in android default charset is UTF-8
    }

    public static Map<String, String> getSpeedPostHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("X-Mashape-Key", "DwVJ9hoRPamshzektZWGCHA1g2ZPp1R0ZntjsnEZNZvSP0rXyD");
        header.put("Accept", "application/json");
        return header;
    }
}

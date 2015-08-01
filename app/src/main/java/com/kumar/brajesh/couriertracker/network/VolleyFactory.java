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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by brajesh.k on 07/07/15.
 */
public class VolleyFactory {
    private static VolleyFactory mVolleyFactory;
    private RequestQueue mRequestQueue;

    private static Context mContext;

    private VolleyFactory(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();

    }

    public static VolleyFactory getInstance(Context context) {
        if (mVolleyFactory == null) {
            synchronized (VolleyFactory.class) {
                if (mVolleyFactory == null) {
                    // getApplicationContext() is key, it keeps you from leaking the
                    // Activity or BroadcastReceiver if someone passes one in.
                    mVolleyFactory = new VolleyFactory(context.getApplicationContext());
                }
            }
        }
        return mVolleyFactory;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}

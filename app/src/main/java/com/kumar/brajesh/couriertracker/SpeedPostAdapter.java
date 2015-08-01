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
package com.kumar.brajesh.couriertracker;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kumar.brajesh.couriertracker.network.SpeedPostResponse;

/**
 * Created by brajesh.k on 07/07/15.
 */
public class SpeedPostAdapter extends BaseAdapter {
    private SpeedPostResponse mSpeedPostResponse;
    private Context mContext;

    public SpeedPostAdapter(Context context, SpeedPostResponse speedPostResponse) {
        mContext = context;
        mSpeedPostResponse = speedPostResponse;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (mSpeedPostResponse != null && mSpeedPostResponse.mDetailTable != null) {
            count = mSpeedPostResponse.mDetailTable.size();
        }
        return count;
    }

    @Override
    public SpeedPostResponse.Detail getItem(int position) {
        SpeedPostResponse.Detail item = null;
        if (mSpeedPostResponse != null && mSpeedPostResponse.mDetailTable != null) {
            item = mSpeedPostResponse.mDetailTable.get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_speed_post_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        SpeedPostResponse.Detail item = getItem(position);
        String date = getSafeString(item.mDate) + " " +getSafeString(item.mTime);
        if (TextUtils.isEmpty(date)) {
            date = SpeedPostFragment.NA;
        }
        holder.time.setText(date);

        if (!TextUtils.isEmpty(item.mStatus)) {
            holder.status.setText(Utils.getFormattedData(mContext, R.string.status, item.mStatus));
        } else {
            holder.status.setText(Utils.getFormattedData(mContext, R.string.status, SpeedPostFragment.NA));
        }

        if (!TextUtils.isEmpty(item.mStatusAt)) {
            holder.location.setText(Utils.getFormattedData(mContext, R.string.location, item.mStatusAt));
        } else {
            holder.location.setText(Utils.getFormattedData(mContext, R.string.location, SpeedPostFragment.NA));
        }


        return view;
    }


    public static class ViewHolder {
        TextView time;
        TextView location;
        TextView status;

        public ViewHolder(View view) {
            time = (TextView) view.findViewById(R.id.time);
            location = (TextView) view.findViewById(R.id.location);
            status = (TextView) view.findViewById(R.id.status);
        }
    }

    private String getSafeString(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }
}

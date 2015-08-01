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

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.kumar.brajesh.couriertracker.network.BaseError;
import com.kumar.brajesh.couriertracker.network.BaseResponse;
import com.kumar.brajesh.couriertracker.network.NetworkListener;
import com.kumar.brajesh.couriertracker.network.SpeedPostRequest;
import com.kumar.brajesh.couriertracker.network.SpeedPostResponse;
import com.kumar.brajesh.couriertracker.network.VolleyHelper;
import com.kumar.brajesh.couriertracker.network.VolleyRequest;
import com.pnikosis.materialishprogress.ProgressWheel;

/**
 * Created by brajesh.k on 07/07/15.
 */
public class SpeedPostFragment extends Fragment implements NetworkListener {
    private static final String URL = "https://indianpost.p.mashape.com/index.php?";
    private ListView mSpeedPostList;
    private TextView mErrorMessage;
    private View mProgressWheel;
    private SpeedPostAdapter mAdapter;
    private SpeedPostResponse mSpeedPostResponse;
    public static final String NA = "NA";
    public static final String TAG = SpeedPostFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_speed_post, container, false);
        initViews(rootView);
        new FetchData().execute(new SpeedPostRequest(getActivity()
                .getIntent().getStringExtra(IntentConstants.TRACK_ID)));
        return rootView;
    }


    private void initViews(View root) {
        mSpeedPostList = (ListView) root.findViewById(R.id.speed_post_list);
        mErrorMessage = (TextView) root.findViewById(R.id.error_message);
        mProgressWheel =  root.findViewById(R.id.progress_wheel);
        quickReturnPattern();
    }

    private View addHeaderView() {
        View headerView = null;
        if (mSpeedPostResponse != null && mSpeedPostList != null) {
            headerView = LayoutInflater.from(getActivity())
                    .inflate(R.layout.list_speed_post_item_header, mSpeedPostList, false);
            TextView itemNo = (TextView) headerView.findViewById(R.id.item_no);
            TextView bookedAt = (TextView) headerView.findViewById(R.id.booked_at);
            TextView bookedOn = (TextView) headerView.findViewById(R.id.booked_on);
            TextView deliveredAt = (TextView) headerView.findViewById(R.id.delivered_at);
            TextView deliveredOn = (TextView) headerView.findViewById(R.id.delivered_on);
            if (!TextUtils.isEmpty(mSpeedPostResponse.mItemNumber)) {
                itemNo.setText(getFormattedData(R.string.item_number, mSpeedPostResponse.mItemNumber));
            } else {
                itemNo.setText(getFormattedData(R.string.item_number, NA));
            }

            if (!TextUtils.isEmpty(mSpeedPostResponse.mBookedAt)) {
                bookedAt.setText(getFormattedData(R.string.booked_at, mSpeedPostResponse.mBookedAt));
            } else {
                bookedAt.setText(getFormattedData(R.string.booked_at, NA));
            }

            if (!TextUtils.isEmpty(mSpeedPostResponse.mBookedOn)) {
                bookedOn.setText(getFormattedData(R.string.booked_on, mSpeedPostResponse.mBookedOn));
            } else {
                bookedOn.setText(getFormattedData(R.string.booked_on, NA));
            }


            if (!TextUtils.isEmpty(mSpeedPostResponse.mDeliveredAt)) {
                deliveredAt.setText(getFormattedData(R.string.delivered_at, mSpeedPostResponse.mDeliveredAt));
            } else {
                deliveredAt.setText(getFormattedData(R.string.delivered_at, NA));
            }

            if (!TextUtils.isEmpty(mSpeedPostResponse.mDeliveredOn)) {
                deliveredOn.setText(getFormattedData(R.string.delivered_on, mSpeedPostResponse.mDeliveredAt));
            } else {
                deliveredOn.setText(getFormattedData(R.string.delivered_on, NA));
            }
            mSpeedPostList.addHeaderView(headerView);
        }
        return headerView;
    }

    private Spannable getFormattedData(int stringId, String right) {
        return Utils.getFormattedData(getActivity(), stringId, right);
    }

    @Override
    public void onSuccess(BaseResponse response) {
        mProgressWheel.setVisibility(View.GONE);
        if (response != null && response instanceof SpeedPostResponse) {

            mSpeedPostResponse = (SpeedPostResponse) response;
            if (TextUtils.isEmpty(mSpeedPostResponse.mError)) {
                mAdapter = new SpeedPostAdapter(getActivity(), mSpeedPostResponse);
                mErrorMessage.setVisibility(View.GONE);
                mSpeedPostList.setVisibility(View.VISIBLE);
                mSpeedPostList.setAdapter(mAdapter);
                addHeaderView();
            } else {
                mSpeedPostList.setVisibility(View.GONE);
                mErrorMessage.setVisibility(View.VISIBLE);
                mErrorMessage.setText(mSpeedPostResponse.mError);
            }

        }
        TrackerLog.v("%s", "success");
    }

    @Override
    public void onError(BaseError error) {
        mProgressWheel.setVisibility(View.GONE);
        if (error != null) {
            mSpeedPostList.setVisibility(View.GONE);
            mErrorMessage.setVisibility(View.VISIBLE);
            mErrorMessage.setText(error.mErrorMessage);
        }
        TrackerLog.v("%s", "error");

    }

    // task for fetching data from server

    private class FetchData extends AsyncTask<SpeedPostRequest, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressWheel.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(SpeedPostRequest... speedPostRequests) {

            VolleyHelper.executeRequest(getActivity(), URL, VolleyRequest.Method.GET, VolleyHelper.getSpeedPostHeader(),
                    speedPostRequests[0], SpeedPostResponse.class, SpeedPostFragment.this, TAG);
            return null;
        }

    }

    private void quickReturnPattern() {
        // In one-pane mode, add the quick-return pattern to the ListView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            mSpeedPostList.setOnScrollListener(new AbsListView.OnScrollListener() {
                int mLastFirstVisibleItem = 0;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (view.getId() == mSpeedPostList.getId()) {
                        // Convert the toolbar height from dp to pixels, so we know how much to move the toolbar
                        // to just about get it off screen.
                        DisplayMetrics metrics = new DisplayMetrics();
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        float logicalDensity = metrics.density;
                        float toolbarHeightInPx =
                                getResources().getDimension(R.dimen.action_bar_height) * logicalDensity;
                        final int currentFirstVisibleItem = mSpeedPostList.getFirstVisiblePosition();
                        if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                            Animator tbOutAnimator = ObjectAnimator.ofFloat(toolbar, View.TRANSLATION_Y, -toolbarHeightInPx);
                            tbOutAnimator.start();
                        } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                            Animator tbInAnimator = ObjectAnimator.ofFloat(toolbar, View.TRANSLATION_Y, 0f);
                            tbInAnimator.start();
                        }
                        mLastFirstVisibleItem = currentFirstVisibleItem;
                    }
                }
            });
        }
    }
}

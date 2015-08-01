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

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by brajesh.k on 07/07/15.
 */
public class SpeedPostResponse extends BaseResponse {

    @SerializedName("error")
    public String mError;

    @SerializedName("ItemNumber")
    public String mItemNumber;

    @SerializedName("BookedAt")
    public String mBookedAt;

    @SerializedName("BookedOn")
    public String mBookedOn;

    @SerializedName("DeliveredAt")
    public String mDeliveredAt;

    @SerializedName("DeliveredOn")
    public String mDeliveredOn;

    @SerializedName("DetailsTable")
    public List<Detail> mDetailTable;

    public static class Detail {
        @SerializedName("Date")
        public String mDate;

        @SerializedName("Time")
        public String mTime;

        @SerializedName("StatusAt")
        public String mStatusAt;

        @SerializedName("Status")
        public String mStatus;
    }
}

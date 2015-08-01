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
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

/**
 * Created by brajesh.k on 07/07/15.
 */
public class Utils {
    public static Spannable getFormattedData(Context context, int stringId, String right) {
        String left = context.getString(stringId);
        SpannableStringBuilder ssb = new SpannableStringBuilder(left + right);
        ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, left.length(), SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE);
        return ssb;
    }
}

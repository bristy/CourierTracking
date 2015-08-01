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

import android.support.v4.app.Fragment;

/**
 * Created by brajesh.k on 07/07/15.
 */
public class FragmentFactory {
    public static enum Type{
        SPEED_POST;
    }

    public static Fragment getFragmnet(Type type) {
        Fragment fragment = null;
        switch (type){
            case SPEED_POST:
                fragment = new SpeedPostFragment();
                break;
            default:
                break;
        }
        return fragment;
    }
}

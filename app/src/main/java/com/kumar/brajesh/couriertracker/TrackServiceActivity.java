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

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class TrackServiceActivity extends ActionBarActivity implements View.OnClickListener {
    private EditText mTrackId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_service);
        initViews();

    }

    private void initViews() {
        findViewById(R.id.track_button).setOnClickListener(this);
        mTrackId = (EditText) findViewById(R.id.track_id);
        mTrackId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence != null && charSequence.length() > 0 && mTrackId.getError() != null) {
                    mTrackId.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_track, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.track_button:
                track();
                break;
            default:
                break;
        }
    }

    private void track() {
        if (mTrackId != null) {
            String id = mTrackId.getText().toString();
            if (!TextUtils.isEmpty(id)) {
                Intent intent = new Intent(this, TrackDetailActivity.class);
                intent.putExtra(IntentConstants.SERVICE_TYPE, FragmentFactory.Type.SPEED_POST);
                intent.putExtra(IntentConstants.TRACK_ID, id);
                startActivity(intent);
            } else {
                mTrackId.requestFocus();
                mTrackId.setError(getString(R.string.error_enter_track_no));
            }
        }
    }
}

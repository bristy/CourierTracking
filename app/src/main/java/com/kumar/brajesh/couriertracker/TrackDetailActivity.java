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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.io.ByteArrayOutputStream;



public class TrackDetailActivity extends ActionBarActivity {
    private static final String TAG = TrackDetailActivity.class.getSimpleName();


    private View mRootContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            FragmentFactory.Type type = (FragmentFactory.Type) getIntent().getSerializableExtra(IntentConstants.SERVICE_TYPE);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, FragmentFactory.getFragmnet(type))
                    .commit();
        }
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(getIntent().getStringExtra(IntentConstants.TRACK_ID));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_track_detail, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_share) {
            shareCurrentScreen();
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void shareCurrentScreen() {
        mRootContent = findViewById(android.R.id.content);
        mRootContent.setDrawingCacheEnabled(true);

        Bitmap sharingBitmap = mRootContent.getDrawingCache(true).copy(Bitmap.Config.RGB_565, false);
        mRootContent.destroyDrawingCache();
        mRootContent.setDrawingCacheEnabled(false);
        new SaveBitmapTask(sharingBitmap).execute();
    }

    private class SaveBitmapTask extends AsyncTask<Void, Void, String> {
        private Bitmap mBitmap;
        public SaveBitmapTask(Bitmap bitmap) {
            mBitmap = bitmap;
        }
        @Override
        protected String doInBackground(Void... params) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                    mBitmap, getIntent().getStringExtra(IntentConstants.TRACK_ID) + System.currentTimeMillis(), null);
            return path;
        }

        @Override
        protected void onPostExecute(String path) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/png");
            Uri imageUri =  Uri.parse(path);
            share.putExtra(Intent.EXTRA_STREAM, imageUri);
            Intent sendIntent = Intent.createChooser(share, getResources().getText(R.string.select));
            if(sendIntent != null) {
                startActivity(sendIntent);
            }
        }
    }
}

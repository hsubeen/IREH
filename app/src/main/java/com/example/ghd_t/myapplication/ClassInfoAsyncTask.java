package com.example.ghd_t.myapplication;

import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * Created by ghd-t on 2018-05-08.
 */

public class ClassInfoAsyncTask extends AsyncTask<String,Void,Bitmap> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        return null;
    }
}

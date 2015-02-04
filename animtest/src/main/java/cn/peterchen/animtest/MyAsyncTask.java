package cn.peterchen.animtest;

import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * Created by peter on 15-1-30.
 */
public class MyAsyncTask extends AsyncTask<Integer,Void,Bitmap>{

    public MyAsyncTask() {
        super();
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        return null;
    }

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
    protected void onCancelled(Bitmap bitmap) {
        super.onCancelled(bitmap);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}

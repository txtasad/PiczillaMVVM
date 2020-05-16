package asad.com.piczillamvvm.repositoryandcallback;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import asad.com.piczillamvvm.utils.ImagesCache;
import asad.com.piczillamvvm.utils.Utility;

/**
 * Created 16th May,2020
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private String imageUrl;
    private ImagesCache cache;
    private CallbackInterface listener;

    public DownloadImageTask(ImagesCache cache,CallbackInterface ci) {
        this.cache = cache;
        this.listener = ci;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        imageUrl = params[0];
        return Utility.getBitmapFromURL(imageUrl);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if (result != null) {
            cache.addImageToWarehouse(imageUrl, result);
            if (listener != null) {
                listener.updateImageBitmap(result);
            }
        }
    }
}
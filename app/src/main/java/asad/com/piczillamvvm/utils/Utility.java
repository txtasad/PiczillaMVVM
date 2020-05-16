package asad.com.piczillamvvm.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import asad.com.piczillamvvm.model.Photo;


public class Utility {

    public static <P, T extends AsyncTask<P, ?, ?>> void execute(T task) {
        execute(task, (P[]) null);
    }

    @SuppressLint("NewApi")
    public static <P, T extends AsyncTask<P, ?, ?>> void execute(T task,
                                                                 P... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .getState() == NetworkInfo.State.CONNECTED
                    || connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .getState() == NetworkInfo.State.CONNECTING) {
                return true;
            } else if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .getState() == NetworkInfo.State.CONNECTED
                    || connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .getState() == NetworkInfo.State.CONNECTING) {

                return true;
            } else
                return false;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * download and return bitmap for the given url
     * DO not call this method from main thread
     *
     * @param src
     * @return
     */
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    /**
     * makes a get call and return the response string
     * DO not call this method from main thread
     *
     * @param url
     * @return
     */
    public static String getResponse(String url) {
        InputStream inputStream = null;
        String result = "";
        HttpURLConnection urlConnection = null;

        try {
            URL newurl = new URL(url);
            urlConnection = (HttpURLConnection) newurl.openConnection();
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        } catch (Exception e) {

        } finally {
            urlConnection.disconnect();
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    @SuppressWarnings("ResourceType")
    @SuppressLint("ShowToast")
    public static void showToastMessage(Context context, String msg) {

        try {
            if (msg != null && !msg.equalsIgnoreCase("")) {
                Toast.makeText(context.getApplicationContext(), msg, 4000)
                        .show();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getImageUrl(Photo photo) {
        StringBuilder url = new StringBuilder();
        if (photo != null) {
            url.append("https://farm");
            url.append(photo.getFarm() + ".staticflickr.com/");
            url.append(photo.getServer());
            url.append("/");
            url.append(photo.getId() + "_" + photo.getSecret() + "_q.jpg");
        }
        return url.toString();
    }

}

package asad.com.piczillamvvm.viewmodel;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import asad.com.piczillamvvm.model.Photo;
import asad.com.piczillamvvm.repositoryandcallback.CallbackInterface;
import asad.com.piczillamvvm.repositoryandcallback.DownloadImageTask;
import asad.com.piczillamvvm.repositoryandcallback.FetchImageUrlsTask;
import asad.com.piczillamvvm.utils.ImagesCache;
import asad.com.piczillamvvm.utils.Utility;

/**
 * Created 16th May,2020 by Mohammad Asad
 */

public class ImagesViewModel extends ViewModel {


    ImagesCache cache;

    MutableLiveData<List<Photo>> photoList;
    MutableLiveData<Bitmap> bitmap;


    public MutableLiveData<List<Photo>> getPhotoList() {
        if(photoList==null)
        {
            photoList=new MutableLiveData<>();
            photoList.setValue(new ArrayList<Photo>());
        }
        return photoList;
    }

    public void setPhotoList(List<Photo> newPhotoList) {

        Log.d("ViewModel","set photolist");
        if(photoList==null){
            photoList=new MutableLiveData<>();
        }
        List<Photo> existing= photoList.getValue();
        existing.addAll(newPhotoList);

        photoList.setValue(existing);
    }

    public MutableLiveData<Bitmap> getBitmap() {
        if(bitmap==null)
        {
            bitmap=new MutableLiveData<>();
        }
        return bitmap;
    }

    public void setBitmap(Bitmap newBitmap) {

        Log.d("ViewModel","set bitmap");
        if(bitmap==null){
            bitmap=new MutableLiveData<>();
        }

        bitmap.setValue(newBitmap);
    }


    public void initCache() {
        cache = ImagesCache.getInstance();
        cache.initializeCache();
    }

    public void fetchImagesUrl(int pageCount,CallbackInterface callbackInterface) {
        // fetch images list from api
        FetchImageUrlsTask fetchImagesTask = new FetchImageUrlsTask(callbackInterface);
        Utility.execute(fetchImagesTask, String.valueOf(pageCount));
    }

    public void fetchBitmap(String imgUrl,CallbackInterface callbackInterface) {
        Bitmap bm = cache.getImageFromWarehouse(imgUrl);
        if (bm == null) {
            DownloadImageTask imgTask = new DownloadImageTask(cache,callbackInterface);
            Utility.execute(imgTask, imgUrl);
        }
        else {
            callbackInterface.updateImageBitmap(bm);
        }
    }

    public void clearCache() {
        cache.clearCache();
    }

}

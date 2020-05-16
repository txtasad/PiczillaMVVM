package asad.com.piczillamvvm.repositoryandcallback;

import android.graphics.Bitmap;

import java.util.List;

import asad.com.piczillamvvm.model.Photo;

/**
 * Created 16th May,2020
 */
public interface CallbackInterface {

    void updateImagesList(List<Photo> photoList);


    void updateImageBitmap(Bitmap bitmap);
}

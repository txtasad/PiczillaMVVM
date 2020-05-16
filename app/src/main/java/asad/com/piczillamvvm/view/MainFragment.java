package asad.com.piczillamvvm.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;


import java.util.List;

import asad.com.piczillamvvm.R;
import asad.com.piczillamvvm.model.Photo;
import asad.com.piczillamvvm.repositoryandcallback.CallbackInterface;
import asad.com.piczillamvvm.utils.Utility;
import asad.com.piczillamvvm.viewmodel.ImagesViewModel;

/**
 * Created 16th May,2020
 */
public class MainFragment extends Fragment implements CallbackInterface {

    private Button previous, next,title;
    private ProgressBar pBar;
    private ImageView imv;
    private int currentIndex = -1;
    private int pageCount = 1;
    private ImagesViewModel imagesViewModel;
    MainFragment refrence;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // for retaining instances over configuration change
        setRetainInstance(true);
        refrence=this;
        imagesViewModel = ViewModelProviders.of(getActivity()).get(ImagesViewModel.class);
        imagesViewModel.initCache();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        // initially if photoList is empty


        pBar =  view.findViewById(R.id.progress);
        pBar.setProgress(20);
        pBar.setSecondaryProgress(50);
        previous =  view.findViewById(R.id.previous);
        next =  view.findViewById(R.id.next);
        imv =  view.findViewById(R.id.imageview);
        title =  view.findViewById(R.id.t);

        if (imagesViewModel.getPhotoList().getValue() == null || imagesViewModel.getPhotoList().getValue().size() == 0) {
            imagesViewModel.fetchImagesUrl(pageCount,this);
        } else {
            imagesViewModel.fetchBitmap(Utility.getImageUrl(imagesViewModel.getPhotoList().getValue().get(currentIndex)),this);
        }
        showLoader();
        // initially if currentIndex is less than 0 hide previous
        if (currentIndex <= 0) {
            previous.setVisibility(View.GONE);
        } else {
            previous.setVisibility(View.VISIBLE);
        }
        //onclick of previous button should navigate the user to previous image
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoader();
                currentIndex--;
                List<Photo> photoList=imagesViewModel.getPhotoList().getValue();
                // on the basis of currentIndex, access the photo url and fetch bitmap
                if (photoList != null && photoList.size() > 0 && currentIndex < photoList.size()) {
                    imagesViewModel.fetchBitmap(Utility.getImageUrl(photoList.get(currentIndex)),refrence);
                }
                if (currentIndex > 0) {
                    previous.setVisibility(View.VISIBLE);
                } else {
                    previous.setVisibility(View.GONE);
                }
            }
        });
        //onclick of next button should navigate the user to next image
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoader();
                currentIndex++;
                List<Photo> photoList=imagesViewModel.getPhotoList().getValue();
                if (photoList != null && photoList.size() > 0 && currentIndex < photoList.size()) {
                    imagesViewModel.fetchBitmap(Utility.getImageUrl(photoList.get(currentIndex)),refrence);
                }
                if (currentIndex > 0) {
                    previous.setVisibility(View.VISIBLE);
                } else {
                    previous.setVisibility(View.GONE);
                }
                if (currentIndex >= photoList.size()) {
                    imagesViewModel.fetchImagesUrl(++pageCount,refrence);
                }
            }
        });

        imagesViewModel.getPhotoList().observe(getActivity(), new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                if (currentIndex < 0 && imagesViewModel.getPhotoList().getValue().size() > 0) {
                    currentIndex++;
                    imagesViewModel.fetchBitmap(Utility.getImageUrl(imagesViewModel.getPhotoList().getValue().get(currentIndex)),refrence);
                }
            }
        });

        imagesViewModel.getBitmap().observe(getActivity(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                hideLoader();
                List<Photo> photoList=imagesViewModel.getPhotoList().getValue();
                if(currentIndex>=0 && currentIndex<photoList.size())
                    title.setText(photoList.get(currentIndex).getTitle());
                imv.setImageBitmap(bitmap);
            }
        });

    }

    @Override
    public void updateImagesList(List<Photo> photoList) {
        imagesViewModel.setPhotoList(photoList);
        // for first case, show first image, without clicking next button

    }

    @Override
    public void updateImageBitmap(Bitmap bitmap) {
        imagesViewModel.setBitmap(bitmap);
    }


    public void showLoader() {
        pBar.setVisibility(View.VISIBLE);
    }


    public void hideLoader() {
        pBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        imagesViewModel.clearCache();
        super.onDestroy();
    }
}

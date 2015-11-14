package com.offerup.offerupandroidchallenge;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhotoContent;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MainFragment extends Fragment implements MainFragmentPresenter.MainFragmentPresenterInterface {

    @Bind(R.id.uploadImageButton)
    Button uploadImageButton;

    @Bind(R.id.choosenImageView)
    ImageView choosenImageView;

    @Bind(R.id.textView)
    TextView textView;

    @Bind(R.id.login_button)
    LoginButton loginButton;


    @OnClick(R.id.choosenImageView)
    public void showImagePickerDialog() {
        mainFragmentPresenter.createImagePickerDialog(this);
    }

    @OnClick(R.id.uploadImageButton)
    public void uploadImageToFacebook() {
        mainFragmentPresenter.createSharePhotoContentObjectForSharing(bitmapForFacebookUpload);
    }

    Bitmap bitmapForFacebookUpload;
    CallbackManager callbackManager;
    MainFragmentPresenter mainFragmentPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, result);
        ButterKnife.setDebug(true);
        callbackManager = CallbackManager.Factory.create();
        mainFragmentPresenter = new MainFragmentPresenter(this);
        uploadImageButton.setText(R.string.upload_image);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.GONE);
                textView.setText(R.string.post_some_pictures);
                uploadImageButton.setText(R.string.upload_to_facebook);
                choosenImageView.setVisibility(View.VISIBLE);
                uploadImageButton.setEnabled(false);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
            }
        });

        loginButton.setFragment(this);
        textView.setText(R.string.offerup_challenge);
        uploadImageButton.setEnabled(false);

        return result;
    }

    public FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {

        @Override
        public void onSuccess(Sharer.Result result) {
            getFragmentManager().beginTransaction().replace(R.id.container_layout, new SuccessfulPostFragment()).commit();

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    //it will return /sdcard/image.tmp
    private File getTempFile(Context context){
    final File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName() );
    if(!path.exists()){
        path.mkdir();
    }
    return new File(path, "image.tmp");
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Bitmap captureBmp = null;
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {

                  //  String selectedImage = (String) data.getExtras().get(MediaStore.EXTRA_OUTPUT);
                    uploadImageButton.setEnabled(true);

                    final File file = getTempFile(getActivity());
                    try {
                        captureBmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(file) );
                        // do whatever you want with the bitmap (Resize, Rename, Add To Gallery, etc)
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }


//                    Picasso.with(getActivity()).load(captureBmp).resize(choosenImageView.getMeasuredWidth(), choosenImageView.getMeasuredHeight()).centerCrop().into(choosenImageView);

                     choosenImageView.setImageBitmap(captureBmp);


                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage;
                    selectedImage = data.getData();
                    Picasso.with(getActivity()).load(selectedImage).resize(choosenImageView.getMeasuredWidth(), choosenImageView.getMeasuredHeight()).centerCrop().into(target);
                    uploadImageButton.setEnabled(true);

                }
                break;
        }
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            bitmapForFacebookUpload = bitmap;
            choosenImageView.setImageBitmap(bitmap);

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

    };

    @Override
    public void setSharePhotoContentObject(SharePhotoContent sharePhotoContentObject) {
        ShareApi.share(sharePhotoContentObject, shareCallback);
    }

    @Override
    public void showImagePickerDialog(AlertDialog.Builder alertDialog) {
        alertDialog.show();
    }
}
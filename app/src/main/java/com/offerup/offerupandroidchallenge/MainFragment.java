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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.AccessToken;
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


public class MainFragment extends Fragment implements MainFragmentPresenter.MainFragmentPresenterInterface {

    @Bind(R.id.doneButton)
    ImageView doneButton;

    @Bind(R.id.uploadImageButton)
    Button uploadImageButton;

    @Bind(R.id.choosenImageView)
    ImageView choosenImageView;

    @Bind(R.id.textView)
    TextView textView;

    @Bind(R.id.login_button)
    LoginButton loginButton;


    @OnClick(R.id.doneButton)
    public void clearImage() {
        choosenImageView.setImageBitmap(null);
        doneButton.setVisibility(View.GONE);
        uploadImageButton.setEnabled(false);
    }

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
    Uri uri;
    MainFragmentPresenter mainFragmentPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, result);
        facebookSetup();
        mainFragmentPresenter = new MainFragmentPresenter(this);
        facebookLoginCallback();
        initialUIElementsSetup();

        return result;
    }

    private void facebookLoginCallback() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                changeUIElementsStateOnSuccessfulFacebookLogin();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        });
    }

    private void initialUIElementsSetup() {
        uploadImageButton.setText(R.string.upload_image);
        textView.setText(R.string.offerup_challenge);
        uploadImageButton.setEnabled(false);
    }

    private void facebookSetup() {
        loginButton.setFragment(this);
        AccessToken.setCurrentAccessToken(null);
        callbackManager = CallbackManager.Factory.create();
    }

    private void changeUIElementsStateOnSuccessfulFacebookLogin() {
        loginButton.setVisibility(View.GONE);
        textView.setText(R.string.post_some_pictures);
        uploadImageButton.setText(R.string.upload_to_facebook);
        choosenImageView.setVisibility(View.VISIBLE);

        uploadImageButton.setEnabled(false);
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
            Log.d("@@@@","###");
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri",uri);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null){
            uri = savedInstanceState.getParcelable("file_uri");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Picasso.with(getActivity()).cancelRequest(target);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK && uri != null) {
                    doneButton.setVisibility(View.VISIBLE);
                    uploadImageButton.setEnabled(true);
                    Picasso.with(getActivity()).load(uri).resize(choosenImageView.getMeasuredWidth(), choosenImageView.getMeasuredHeight()).centerCrop().into(target);

                }
                break;

            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage;
                    selectedImage = data.getData();
                    Picasso.with(getActivity()).load(selectedImage).resize(choosenImageView.getMeasuredWidth(), choosenImageView.getMeasuredHeight()).centerCrop().into(target);

                }
                break;
        }
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            bitmapForFacebookUpload = bitmap;
            choosenImageView.setImageBitmap(bitmap);
            doneButton.setVisibility(View.VISIBLE);
            uploadImageButton.setEnabled(true);

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

    @Override
    public void setURIForCameraInstance(Uri uri) {
        this.uri = uri;
    }
}

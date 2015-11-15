package com.offerup.offerupandroidchallenge;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainFragmentPresenter {
    MainFragmentPresenterInterface mainFragmentPresenterInterface;

    public MainFragmentPresenter(MainFragmentPresenterInterface mainFragmentPresenterInterface) {
        this.mainFragmentPresenterInterface = mainFragmentPresenterInterface;

    }

    public void createSharePhotoContentObjectForSharing(Bitmap bitmap) {
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        mainFragmentPresenterInterface.setSharePhotoContentObject(content);

    }


    public void createImagePickerDialog(final Fragment fragment) {
        final Activity activity = fragment.getActivity();
        final CharSequence[] items = {activity.getString(R.string.camera), activity.getString(R.string.gallery)};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.select_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(activity.getString(R.string.camera))) {
                    startCamera(fragment);
                } else if (items[item].equals(activity.getString(R.string.gallery))) {
                    showGallery(fragment);
                }
            }
        });

        mainFragmentPresenterInterface.showImagePickerDialog(builder);
    }

    private void showGallery(Fragment fragment) {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        fragment.startActivityForResult(
                Intent.createChooser(intent, fragment.getActivity().getString(R.string.select_image)),2);
    }

    private void startCamera(Fragment fragment) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri= getOutputMediaFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        mainFragmentPresenterInterface.setURI(uri.toString());
        fragment.startActivityForResult(intent, 1);
    }

    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

        return new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
    }

    public interface MainFragmentPresenterInterface {
        void setSharePhotoContentObject(SharePhotoContent sharePhotoContentObject);

        void showImagePickerDialog(AlertDialog.Builder alertDialog);

        void setURI(String uri);
    }
}

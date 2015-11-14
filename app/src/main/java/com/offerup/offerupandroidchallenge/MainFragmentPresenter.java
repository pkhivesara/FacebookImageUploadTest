package com.offerup.offerupandroidchallenge;

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
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import java.io.File;
import java.io.IOException;
import java.util.Date;


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

    private File getTempFile(Fragment context){
        final File path = new File( Environment.getExternalStorageDirectory(), context.getActivity().getPackageName() );
        if(!path.exists()){
            path.mkdir();
        }
        return new File(path, "image.tmp");
    }

    public void createImagePickerDialog(final Fragment fragment) {
        final CharSequence[] items = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        builder.setTitle("Select Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {


                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(fragment)));
                    fragment.startActivityForResult(intent, 1);
                } else if (items[item].equals("Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    fragment.startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            2);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        mainFragmentPresenterInterface.showImagePickerDialog(builder);
    }


    public interface MainFragmentPresenterInterface {
        void setSharePhotoContentObject(SharePhotoContent sharePhotoContentObject);

        void showImagePickerDialog(AlertDialog.Builder alertDialog);
    }
}

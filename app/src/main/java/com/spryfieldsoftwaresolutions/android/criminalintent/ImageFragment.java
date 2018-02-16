package com.spryfieldsoftwaresolutions.android.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

/**
 * Created by Adam Baxter on 14/02/18.
 * <p>
 * A DialogFragment to display an Image when the user presses the thumbnail in a crime fragment.
 */


public class ImageFragment extends DialogFragment {
    private static final String EXTRA_FILEPATH =
            "com.spryfieldsoftwaresolutions.android.criminalintent.filepath";

    private int mPhotoviewHeight;
    private int mPhotoviewWidth;

    private static final String ARGS_FILEPATH = "filepath";

    private DialogFragment mPictureFragment;

    public static ImageFragment newInstance(File image) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_FILEPATH, image);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        final View v = View.inflate(getActivity(), R.layout.dialog_image, null);
        // String filepath = (String) getArguments().getSerializable(ARGS_FILEPATH);

        v.post(new Runnable() {
            @Override
            public void run() {
                mPhotoviewWidth = v.getWidth();
                mPhotoviewHeight = v.getHeight();
            }
        });

        //File image = new File(filepath);
        File image = (File) getArguments().getSerializable(ARGS_FILEPATH);

        if (image.exists()) {
            Bitmap pic = BitmapFactory.decodeFile(image.getAbsolutePath());

            int height = pic.getHeight();
            int width = pic.getWidth();

            Log.e("IMAGEFRAGMENT", "IMAGE SIZE HxW: " + height + "x" + width);
            ImageView imgView = v.findViewById(R.id.crime_photo_full_size);
            imgView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            try {
                imgView.setImageBitmap(PictureUtils.rotateImage(pic, image.getAbsolutePath()));
            } catch (IOException ie) {
                ie.printStackTrace();
            }

        } else {
            Log.e("IMAGEFRAGMENT", "IMAGE DOES NOT EXIST");

        }

        return new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }

}

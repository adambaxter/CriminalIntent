package com.spryfieldsoftwaresolutions.android.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Adam Baxter on 14/02/18.
 * Class to handle scaling of photos the user takes
 */

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        //Read in dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        // Figure out by how much to scale down
        int inSampleSize;

        if (srcHeight < srcWidth) {

            inSampleSize = Math.round(srcWidth / destWidth);
        } else {
            inSampleSize = Math.round(srcHeight / destHeight);
        }

        Log.e("SCALED IMAGE SIZE", "Sample size:" + inSampleSize + "\nsrcHeight: " + srcHeight + "\nsrcWidth: " + srcWidth + "\ndestHeight: " + destHeight +
                "\ndestWidth: " + destWidth);
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        // Read in anc create final bitmap
        try {
            return rotateImage(BitmapFactory.decodeFile(path, options), path);
        } catch (IOException ie) {
            ie.printStackTrace();
            return BitmapFactory.decodeFile(path, options);
        }
    }

    public static Bitmap getScaledBitmap(String path, Activity activity) {


        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
                .getSize(size);
        Bitmap scaledBitmap = getScaledBitmap(path, size.x, size.y);

        try {
            return rotateImage(scaledBitmap, path);
        } catch (IOException ie) {
            ie.printStackTrace();
            return scaledBitmap;
        }

    }

    public static Bitmap rotateImage(Bitmap scaledBitmap, String path) throws IOException {

        try {
            ExifInterface exifInterface = new ExifInterface(path);
            String orientationString = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientationTag = orientationString != null ? Integer.parseInt(orientationString) :
                    ExifInterface.ORIENTATION_NORMAL;
            int rotationAngle = 0;

            switch (orientationTag) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotationAngle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotationAngle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotationAngle = 270;
                    break;
                default:
                    rotationAngle = 0;
            }
            Matrix matrix = new Matrix();
            matrix.postRotate(rotationAngle);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(),
                    scaledBitmap.getHeight(), matrix, true);

            return rotatedBitmap;

        } catch (IOException ie) {
            ie.printStackTrace();
            return null;
        }

    }

}

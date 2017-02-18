package com.aguitelson.thirdversion.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.aguitelson.thirdversion.R;
import com.aguitelson.thirdversion.actvities.MainActivity;

/**
 * Created by aguitelson on 12.02.17.
 */

public class PictureLoader {
    private final MainActivity activity;

    public PictureLoader(MainActivity activity) {
        this.activity = activity;
    }

    public void loadPicture(String photoPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
        ImageView imageView = (ImageView) activity.findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
        activity.fileManager.setCurrentFileName(photoPath);
    }


}

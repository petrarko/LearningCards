package com.aguitelson.thirdversion.tools;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;



import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by aguitelson on 04.02.17.
 */

public class TakeAPicture {
    public static final int REQUEST_TAKE_PHOTO = 1;


    private static File createImageFile(AppCompatActivity activity, String imageFileName) throws IOException {
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File newFile = new File(storageDir.getAbsolutePath() + File.separator + imageFileName);
        newFile.createNewFile();
        return newFile;
    }


    static void dispatchTakePictureIntent(AppCompatActivity activity, String fileName) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(activity, fileName);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProviderWrapperClassJustForBug.getUriForFile(activity,
                        "com.aguitelson.thirdversion",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1000);

                activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    public static void makeTwoPictures(AppCompatActivity activity) {
        UUID id = UUID.randomUUID();
        // TODO investigate why reverse order??
        dispatchTakePictureIntent(activity, FileNameGenerator.generatePictureName(id.toString(), FileNameGenerator.FilePrefix.SECOND));
        dispatchTakePictureIntent(activity, FileNameGenerator.generatePictureName(id.toString(), FileNameGenerator.FilePrefix.FIRST));
    }

    // https://github.com/coomar2841/android-multipicker-library/issues/69
    class FileProviderWrapperClassJustForBug extends FileProvider {
    }
}

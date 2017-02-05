package com.example.aguitelson.helloworld;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {
    private final MainActivity activity = this;
    static int currentPicture = 0;
    private static final Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.activity_main);
        final Button button = (Button) findViewById(R.id.load);
        button.setOnClickListener(new LoadPictureButtonListener());

        final Button nextButton = (Button) findViewById(R.id.next);
        nextButton.setOnClickListener(new NextPictureButtonListener());
        loadPicture(0);
    }

    class LoadPictureButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TakeAPicture.dispatchTakePictureIntent(activity);
            TakeAPicture.galleryAddPic(activity);
        }
    }

    class NextPictureButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            loadPicture(rand.nextInt(getListOfFiles().size()));
        }
    }


    private void loadPicture(int index) {


        String photoPath = getListOfFiles().get(index).getAbsolutePath();


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
        imageView.setMaxHeight(420);
        imageView.setMinimumHeight(420);


    }

    private List<File> getListOfFiles() {
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] list = storageDir.listFiles();
        ArrayList<File> files = new ArrayList<>();
        for (File file : list) {
            if (file.getName().endsWith(".jpg") && file.length() != 0) {
                files.add(file);
            }
        }
        return files;
    }


}

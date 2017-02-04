package com.example.aguitelson.helloworld;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final MainActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_main);
        final Button button = (Button) findViewById(R.id.load);
        button.setOnClickListener(new ButtonListener());
        loadPicture();
    }

    class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TakeAPicture.dispatchTakePictureIntent(activity);
            TakeAPicture.galleryAddPic(activity);
        }
    }



    private void loadPicture(){
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] list = storageDir.listFiles();
        for (int i = 0; i < list.length; i++) {
            if(list[i].getName().endsWith(".jpg")){
                String photoPath = list[i].getAbsolutePath();



                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
                return;
            }

        }

    }



}

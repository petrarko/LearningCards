package com.example.aguitelson.helloworld;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

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


    }

    class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TakeAPicture.dispatchTakePictureIntent(this);
            TakeAPicture.galleryAddPic(this);
        }
    }




}

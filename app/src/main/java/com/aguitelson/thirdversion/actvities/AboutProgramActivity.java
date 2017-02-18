package com.aguitelson.thirdversion.actvities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import com.aguitelson.thirdversion.R;

/**
 * Created by aguitelson on 17.02.17.
 */

public class AboutProgramActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        WebView mWebView = (WebView) findViewById(R.id.text_about);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_res/raw/text.html");
    }

    public void backToMain(View view) {
        finish();
    }
}

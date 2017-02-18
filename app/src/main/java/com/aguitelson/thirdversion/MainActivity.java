package com.aguitelson.thirdversion;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final MainActivity activity = this;
    public final static String EXTRA_MESSAGE = "com.aguitelson.thirdversion.MESSAGE";

    MenuItem invertItem;
    MenuItem invertAllItem;
    MenuItem deleteItem;
    TextView text;
    FileManager fileManager;
    PictureLoader pictureLoader;

    GestureDetector gdt = new GestureDetector(new GestureListener(activity));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnTouchListener(new ShowPictureListener());
        text = (TextView) findViewById(R.id.textView);
        fileManager = new FileManager(activity);
        pictureLoader = new PictureLoader(activity);

        gdt = new GestureDetector(new GestureListener(activity));
        fileManager.removeAllEmptyFiles();
        fileManager.restoreData();

        updateElementsOnPictureChanges();
    }

    public void sendMessage() {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "hello!");
        startActivity(intent);
    }


    public void showNextPicture() {
        if (fileManager.getSetOfMainFiles().isEmpty()) {
            Toasts.showToasts("No pictures!", activity);
            return;
        }
        if (fileManager.getCurrentFileName() != null && fileManager.getSetOfMainFiles().size() == 1){
            return;
        }
        pictureLoader.loadPicture(fileManager.generateRandomFile());
    }


    class ShowPictureListener implements View.OnTouchListener {


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(fileManager.getCurrentFileName() == null){
                Toasts.showToasts("Load your pictures!", activity);
                return true;
            }
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    pictureLoader.loadPicture(fileManager.getPairForCurrentFile());
                    break;
                case MotionEvent.ACTION_UP:
                    pictureLoader.loadPicture(fileManager.getPairForCurrentFile());
                    break;
                default:
                    return true;
            }

            return true;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        invertItem = menu.findItem(R.id.invert);
        invertAllItem = menu.findItem(R.id.invertAll);
        invertItem.setEnabled(fileManager.picturesExist());
        invertAllItem.setEnabled(fileManager.picturesExist());
        deleteItem = menu.findItem(R.id.deleteCurrent);
        deleteItem.setEnabled(fileManager.picturesExist());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.take_picture) {
            TakeAPicture.makeTwoPictures(activity);
            closeOptionsMenu();
            return true;
        }
        if (id == R.id.invert) {
            pictureLoader.loadPicture(fileManager.invertCurrentPicture());
            return true;
        }
        if (id == R.id.invertAll) {
            pictureLoader.loadPicture(fileManager.invertAllPictures());
            return true;
        }
        if (id == R.id.about) {
            sendMessage();
            return true;
        }

        if (id == R.id.deleteCurrent) {
            fileManager.deleteCurrentPicture();
            if(!fileManager.picturesExist()){
                ImageView imageView = (ImageView)findViewById(R.id.imageView);
                imageView.setImageResource(0);
                fileManager.setCurrentFileName(null);
                text.setVisibility(View.VISIBLE);
            }
            updateElementsOnPictureChanges();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void updateElementsOnPictureChanges() {
        fileManager.updateFiles();
        if (fileManager.picturesExist()) {
            if (fileManager.getCurrentFileName() == null) {
                activity.showNextPicture();
            }
            text.setVisibility(View.GONE);
        } else {
            text.setVisibility(View.VISIBLE);
        }
        if (invertItem != null && invertAllItem != null) {
            invertItem.setEnabled(fileManager.picturesExist());
            invertAllItem.setEnabled(fileManager.picturesExist());
        }
        if(deleteItem!=null){
            deleteItem.setEnabled(fileManager.picturesExist());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == TakeAPicture.REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                updateElementsOnPictureChanges();
            }
        }
    }



}

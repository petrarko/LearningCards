package com.aguitelson.thirdversion.tools;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.aguitelson.thirdversion.actvities.MainActivity;

/**
 * Created by aguitelson on 07.02.17.
 */

public class GestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private final MainActivity activity;

    public GestureListener(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            activity.showNextPicture();
            return false; // Right to left
        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            activity.showNextPicture();
            return false; // Left to right
        }
        return false;
    }
}

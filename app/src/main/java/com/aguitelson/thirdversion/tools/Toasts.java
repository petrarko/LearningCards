package com.aguitelson.thirdversion.tools;

import android.content.Context;
import android.widget.Toast;

import com.aguitelson.thirdversion.actvities.MainActivity;

/**
 * Created by aguitelson on 11.02.17.
 */

public class Toasts {
    public static void showToasts(String text, MainActivity activity) {
        Context context = activity.getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}

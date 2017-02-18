package com.aguitelson.thirdversion.tools;

import android.util.Log;

import java.io.File;

/**
 * Created by aguitelson on 18.02.17.
 */

public class FileManager {
    static boolean isFileReallyAvailable(File file) {
        boolean available = false;
        final long timeout = 1000; // In the Nexus 4 I tried, 700 ms seems like the average
        final long sleepEveryRetry = 100;
        long sleepSoFar = 0;
        do {
            try {
                Thread.sleep(sleepEveryRetry);
            } catch (Exception e) {
            }
            final long fileSize = file.length();
            available = fileSize > 0;
            sleepSoFar += sleepEveryRetry;
            Log.v(PictureManager.class.getName(), "The file " + file.getName() + " is still not valid after " + sleepSoFar + " ms");
        } while (!available && sleepSoFar < timeout);

        return available;
    }


}

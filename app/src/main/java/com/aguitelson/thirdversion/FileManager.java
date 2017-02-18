package com.aguitelson.thirdversion;

import android.os.Environment;
import android.util.Log;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by aguitelson on 11.02.17.
 */

public class FileManager {
    private final FilePrefix FIRST_PREFIX = FilePrefix.FIRST;
    private final FilePrefix SECOND_PREFIX = FilePrefix.SECOND;
    private final Random random = new Random();


    private String currentFileName;
    private final MainActivity activity;
    BiMap<String, String> files = HashBiMap.create();

    public FileManager(MainActivity activity) {
        this.activity = activity;
    }

    public String getCurrentFileName() {
        return currentFileName;
    }

    public void setCurrentFileName(String currentFileName) {
        this.currentFileName = currentFileName;
    }

    public boolean picturesExist() {
        return !files.isEmpty();
    }

    public String getPairForCurrentFile() {
        return FileNameGenerator.changePrefix(currentFileName);
    }

    public void removeAllEmptyFiles() {
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] list = storageDir.listFiles();
        for (File file : list) {
            if (file.length() == 0) {
                file.delete();
            }
        }
    }

    public void dumpFileDataInFile() {
        Gson gson = new Gson();
        String dir = activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "data.json";
        try {
            PrintWriter writer = new PrintWriter(dir, "UTF-8");
            writer.println(gson.toJson(files));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void restoreData() {
        Gson gson = new Gson();
        String dir = activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "data.json";
        if (!new File(dir).exists()) {
            return;
        }
        try {
            files = HashBiMap.create(gson.fromJson(new FileReader(dir), Map.class));
        } catch (Exception e) {
            log(e.getMessage());
        }
        Iterator it = files.entrySet().iterator();
        while(it.hasNext()) {
            String s = (String) ((Map.Entry)it.next()).getKey();
            File f1 = new File(s);
            File f2 = new File(files.get(s));
            if (!(fileIsGood(f1, FilePrefix.FIRST) && fileIsGood(f2, FilePrefix.SECOND))) {
                if (!(fileIsGood(f1, FilePrefix.SECOND) && fileIsGood(f2, FilePrefix.FIRST))) {
                    it.remove();
                }
            }
        }
    }


    public void updateFiles() {
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] list = storageDir.listFiles();
        for (File file : list) {
            File second = new File(FileNameGenerator.changePrefix(file.getAbsolutePath()));
            Log.i(FileManager.class.getName(), "file: " + file.getAbsolutePath() + " second " + second.getAbsolutePath() + "!fileAlreadyRead(file.getAbsolutePath()" + !fileAlreadyRead(file.getAbsolutePath()) + "fileIsGood(file, FIRST_PREFIX) " + fileIsGood(file, FIRST_PREFIX) + "fileIsGood(second, SECOND_PREFIX)" + fileIsGood(second, SECOND_PREFIX));

            if (!fileAlreadyRead(file.getAbsolutePath())) {
                if (fileIsGood(file, FIRST_PREFIX) && fileIsGood(second, SECOND_PREFIX)) {
                    files.put(file.getAbsolutePath(), second.getAbsolutePath());
                    dumpFileDataInFile();
                }
            }
        }
        log("Files loaded: " + files);
    }

    private boolean fileAlreadyRead(String filePath) {
        return files.containsKey(filePath) || files.containsValue(filePath);
    }

    private boolean fileIsGood(File file, FilePrefix filePrefix) {
        Log.i(FileManager.class.getName(), "filePrefix " + filePrefix + " file: " + file.getAbsolutePath() + "FileNameGenerator.isFileNameMatch(file.getName(), filePrefix)" + FileNameGenerator.isFileNameMatch(file.getName(), filePrefix) + "file.length()" + file.length());
        boolean result = FileNameGenerator.isFileNameMatch(file.getName(), filePrefix) && isFileReallyAvailable(file);
        log("result of fileIsGood: " + result);
        return result;
    }

    Set<String> getSetOfMainFiles() {
        log("getSetOfMainFiles:" + files.keySet());
        return files.keySet();
    }

    String generateRandomFile() {
        if (files.size() == 1) {
            return Iterables.getOnlyElement(files.keySet());
        }
        Set<String> setToChoose = new HashSet<>();
        setToChoose.addAll(getSetOfMainFiles());
        setToChoose.remove(currentFileName);
        int size = setToChoose.size();
        int item = random.nextInt(size);
        int i = 0;
        for (String obj : setToChoose) {
            if (i == item)
                return obj;
            i++;
        }
        throw new RuntimeException("Bad randomization");
    }


    String invertCurrentPicture() {
        String pair = files.remove(currentFileName);
        files.put(pair, currentFileName);
        dumpFileDataInFile();
        return pair;
    }

    String invertAllPictures() {
        files = files.inverse();
        dumpFileDataInFile();
        return files.inverse().get(currentFileName);
    }


    public void deleteCurrentPicture() {
        File f1 = new File(currentFileName);
        File f2 = new File(FileNameGenerator.changePrefix(currentFileName));
        f1.delete();
        f2.delete();
        files.remove(currentFileName);
        dumpFileDataInFile();
        currentFileName = null;
    }

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
            Log.v(FileManager.class.getName(), "The file " + file.getName() + " is still not valid after " + sleepSoFar + " ms");
        } while (!available && sleepSoFar < timeout);

        return available;
    }

    private void log(String message) {
        Log.i(FileManager.class.getName(), message);
    }
}

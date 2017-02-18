package com.aguitelson.thirdversion.tools;

import android.os.Environment;
import android.util.Log;

import com.aguitelson.thirdversion.actvities.MainActivity;
import com.aguitelson.thirdversion.tools.FileNameGenerator.FilePrefix;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by aguitelson on 11.02.17.
 */

public class PictureManager {
    private final Random random = new Random();
    private final MainActivity activity;
    BiMap<String, String> files = HashBiMap.create();
    private String currentFileName;

    public PictureManager(MainActivity activity) {
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
        while (it.hasNext()) {
            String s = (String) ((Map.Entry) it.next()).getKey();
            File f1 = new File(s);
            File f2 = new File(files.get(s));
            if (!(fileIsGoodBasic(f1, FilePrefix.FIRST) && fileIsGoodBasic(f2, FilePrefix.SECOND))) {
                if (!(fileIsGoodBasic(f1, FilePrefix.SECOND) && fileIsGoodBasic(f2, FilePrefix.FIRST))) {
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
            if (!fileAlreadyRead(file.getAbsolutePath())) {
                if (fileIsGood(file, FilePrefix.FIRST) && fileIsGood(second, FilePrefix.SECOND)) {
                    files.put(file.getAbsolutePath(), second.getAbsolutePath());
                    dumpFileDataInFile();
                }
            }
        }
    }

    private boolean fileAlreadyRead(String filePath) {
        return files.containsKey(filePath) || files.containsValue(filePath);
    }

    private boolean fileIsGood(File file, FilePrefix filePrefix) {
        return FileNameGenerator.isFileNameMatch(file.getName(), filePrefix) && FileManager.isFileReallyAvailable(file);
    }

    private boolean fileIsGoodBasic(File file, FilePrefix filePrefix) {
        boolean result = FileNameGenerator.isFileNameMatch(file.getName(), filePrefix);
        return result;
    }

    public Set<String> getSetOfMainFiles() {
        return files.keySet();
    }

    public String generateRandomFile() {
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


    public String invertCurrentPicture() {
        String pair = files.remove(currentFileName);
        files.put(pair, currentFileName);
        dumpFileDataInFile();
        return pair;
    }

    public String invertAllPictures() {
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

    private void log(String message) {
        Log.i(PictureManager.class.getName(), message);
    }
}

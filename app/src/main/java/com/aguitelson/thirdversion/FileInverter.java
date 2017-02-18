package com.aguitelson.thirdversion;




import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by aguitelson on 10.02.17.
 */

public class FileInverter {
    public static void invertImage(String currentFileName) {
        cutOneFileToAnother(currentFileName, currentFileName+"tmp");
        String invertedName = FileNameGenerator.changePrefix(currentFileName);
        cutOneFileToAnother(invertedName, currentFileName);
        cutOneFileToAnother(currentFileName+"tmp", invertedName);
    }

    private static void cutOneFileToAnother(String source, String target){
        try {
            File sourceFile = new File(source);
            File targetFile = new File(target);
            FileChannel src = new FileInputStream(sourceFile).getChannel();
            FileChannel dest = new FileOutputStream(targetFile).getChannel();
            dest.transferFrom(src, 0, src.size());
            sourceFile.delete();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

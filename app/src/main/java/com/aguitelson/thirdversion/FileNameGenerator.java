package com.aguitelson.thirdversion;

import java.io.File;
import java.util.UUID;

/**
 * Created by aguitelson on 09.02.17.
 */

public class FileNameGenerator {
     static boolean isFileNameMatch(String fileName, FilePrefix prefix){
         String[] split = fileName.split("_");
         try{
            UUID.fromString(split[0]);
             System.out.print("Parsed successfully");
        }catch (IllegalArgumentException e){
             System.out.print(e.toString());
            return false;
        }
         return ("_"+split[1]).equals(prefix.toString()+".jpg");

    }

    static String generatePictureName(FilePrefix prefix){
        UUID id = UUID.randomUUID();
        return generatePictureName(id.toString(), prefix);
    }

    static String generatePictureName(String id, FilePrefix prefix){
        return id.toString()+ prefix + ".jpg";
    }

    static String changePrefix(String fPath){
        String[] path = fPath.split("/");
        String fName = path[path.length-1];
        String[] split = fName.split("_");

        if(("_"+split[1]).startsWith(FilePrefix.FIRST.toString())){
            return fPath.replace(fName, generatePictureName(split[0], FilePrefix.SECOND));
        }
        if(("_"+split[1]).startsWith(FilePrefix.SECOND.toString())){
            return fPath.replace(fName, generatePictureName(split[0], FilePrefix.FIRST));
        }
        throw new RuntimeException("Incorrect file name" + fName);
    }

}

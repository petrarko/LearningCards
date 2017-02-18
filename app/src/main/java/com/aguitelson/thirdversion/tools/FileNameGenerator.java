package com.aguitelson.thirdversion.tools;


import java.util.UUID;

/**
 * Created by aguitelson on 09.02.17.
 */

public class FileNameGenerator {
    static boolean isFileNameMatch(String fileName, FilePrefix prefix) {
        String[] split = fileName.split("_");
        try {
            UUID.fromString(split[0]);
        } catch (IllegalArgumentException e) {
            System.out.print(e.toString());
            return false;
        }
        return ("_" + split[1]).equals(prefix.toString() + ".jpg");

    }

    static String generatePictureName(FilePrefix prefix) {
        UUID id = UUID.randomUUID();
        return generatePictureName(id.toString(), prefix);
    }

    static String generatePictureName(String id, FilePrefix prefix) {
        return id.toString() + prefix + ".jpg";
    }

    static String changePrefix(String fPath) {
        String[] path = fPath.split("/");
        String fName = path[path.length - 1];
        String[] split = fName.split("_");

        if (("_" + split[1]).startsWith(FilePrefix.FIRST.toString())) {
            return fPath.replace(fName, generatePictureName(split[0], FilePrefix.SECOND));
        }
        if (("_" + split[1]).startsWith(FilePrefix.SECOND.toString())) {
            return fPath.replace(fName, generatePictureName(split[0], FilePrefix.FIRST));
        }
        throw new RuntimeException("Incorrect file name" + fName);
    }

    public enum FilePrefix {
        FIRST("_1"),
        SECOND("_2");

        private final String value;

        FilePrefix(String s) {
            this.value = s;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}

package com.aguitelson.thirdversion;

/**
 * Created by aguitelson on 09.02.17.
 */

public enum FilePrefix {
    FIRST("_1"),
    SECOND("_2");

    private final String value;

    FilePrefix(String s){
        this.value = s;
    }

    @Override
    public String toString() {
        return value;
    }
}

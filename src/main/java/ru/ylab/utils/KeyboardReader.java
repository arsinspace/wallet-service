package ru.ylab.utils;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Getter
public class KeyboardReader{

    private static KeyboardReader instance;
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private KeyboardReader(){

    }
    public static KeyboardReader getInstance(){
        if (instance == null)
            instance = new KeyboardReader();
        return instance;
    }

    public static String readLine(){
        try {
            return reader.readLine();
        } catch (IOException e) {
            return "Error: cant read line";
        }
    }
}

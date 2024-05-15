package com.example.digitalline;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ExternalStorage {
    public static String readFile(Context context, String name) throws FileNotFoundException {
        try {
            FileInputStream fileInputStream = context.openFileInput(name);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                stringBuffer.append(lines);
            }
            if (stringBuffer.toString().isEmpty())
                throw new FileNotFoundException();
            return stringBuffer.toString();
        } catch (IOException e) {
            if (!(e.getClass() == FileNotFoundException.class)) {
                e.printStackTrace();
                throw new RuntimeException("IOException in reading file " + name);
            } else
                throw new FileNotFoundException();
        }
    }

    public static void writeFile(Context context, String name, String content) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveObject(Context context, String fileName, Object o) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(o);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T restoreObject(Context context, String fileName) throws IOException {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object o = objectInputStream.readObject();
            objectInputStream.close();
            return (T)o;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}

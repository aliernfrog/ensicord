package com.aliernfrog.EnsiBot.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
    public static void copyFile(String source, String destination) throws Exception {
        File src = new File(source);
        InputStream in;
        OutputStream out;
        if (src.isFile()) {
            in =  new FileInputStream(new File(source));
            out = new FileOutputStream(new File(destination));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        } else {
            File[] files = src.listFiles();
            for (int i = 0; i < files.length; i++) {
                copyFile(files[i].getPath(), destination+"/"+files[i].getName());
            }
        }
    }

    public static String readFile(String source) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(new File(source).getPath()));
        String _line;
        String _full = "";
        while ((_line = reader.readLine()) != null) {
            if (_full.length() > 0) {
                _full += "\n"+_line;
            } else {
                _full += _line;
            }
        }
        reader.close();
        return _full;
    }

    public static String readFileFromAssets(Context context, String fileName) throws Exception {
        InputStream inputStream = context.getAssets().open(fileName);
        int size = inputStream.available();
        byte[] bytes = new byte[size];
        inputStream.read(bytes);
        inputStream.close();
        return new String(bytes);
    }

    public static void appendFile(String path, String text) throws Exception {
        File file = new File(path);
        if (!file.exists()) file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.append(text);
        writer.newLine();
        writer.close();
    }

    public static void saveFile(String folder, String fileName, String content) throws Exception {
        File folderFile = new File(folder);
        File file = new File(folderFile, fileName);
        FileWriter writer = new FileWriter(file);
        writer.append(content);
        writer.flush();
        writer.close();
    }
}

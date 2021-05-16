package com.aliernfrog.EnsiBot.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileUtil {
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

    public static void saveFile(String folder, String fileName, String content) throws Exception {
        File folderFile = new File(folder);
        File file = new File(folderFile, fileName);
        FileWriter writer = new FileWriter(file);
        writer.append(content);
        writer.flush();
        writer.close();
    }
}

package com.example.common;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogHelper {
    public static void  writeLog (String path, String fileName, String content) {
        try {
            File folder = new File(path);
            folder.mkdir();
            File f = new File(path + "/" + fileName);
            f.createNewFile();
            FileWriter myWriter = new FileWriter(path + "/" + fileName, true);
            myWriter.write(content + "\n");
            myWriter.close();
            System.out.println("成功写入文件。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

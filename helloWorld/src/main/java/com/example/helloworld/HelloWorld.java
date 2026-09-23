package com.example.helloworld;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.example.common.WebViewHelper;
import com.example.sFilePicker.SFilePickerHelper;
import com.example.sFilePicker.FileInfo;

import com.example.sFilePicker.PickFileOptions;
import java.io.*;
import com.example.common.LogHelper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.IOException;

import io.dcloud.uts.UTSAndroid;
import okhttp3.*;
import com.syczuan.plugin.request.*;

import com.alibaba.fastjson.JSONObject;


public class HelloWorld {
    public HelloWorld() {
    }

    public String sayHello(String name, String style) {
        System.out.println(1);
        return "Say hello:" + name + " Styles:" + style;
    }
    public String testWebview(WebView webview) {
        WebViewHelper webViewHelper = new WebViewHelper(webview);
        return webViewHelper.test();
    }

    public int testWebviewPromiseResolve(WebView webview, String globalVarName, String promiseId) {
        WebViewHelper webViewHelper = new WebViewHelper(webview);
        webViewHelper.consoleLog("begin testWebviewPromiseResolve");
        webViewHelper.consoleLog(globalVarName);
        webViewHelper.consoleLog(promiseId);
        webViewHelper.webview.evaluateJavascript("try {" + globalVarName + "['" + promiseId + "'].resolve(" + globalVarName + "['" + promiseId + "']" + ");} catch(e) {console.error('promise异常', e)}", null);
        return 1;
    }

    public String pickFile (String path) throws IOException {
//        SFilePickerHelper helper = new SFilePickerHelper();
        LogHelper.writeLog(path, "text1.txt", "策划书内容");
        return path;
    }
    public String getAbsFilxePath (Activity act, String pathFrom, String cacheDir) throws IOException {
        Uri uri = Uri.parse(pathFrom);
        System.out.println(pathFrom);
        System.out.println(cacheDir);
//        return uri.toString();
        ContentResolver resolver = act.getContentResolver();
//        return "1";
        InputStream is = resolver.openInputStream(uri);
        return "2";
//        String mimeType = resolver.getType(uri);
//        String ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
//        return mimeType;
////        InputStream inputStream = resolver.openInputStream(uri);
//        String name = "file_" + System.currentTimeMillis() + "." + ext;
////        String baseDir = act.getBaseContext().externalCacheDir || act.getBaseContext().cacheDir;
//        File destFile = new File(cacheDir, name);
//        destFile.createNewFile();
//        FileInputStream is = null;
//        FileOutputStream os = null;
//        File readFile = new File(pathFrom);
//        is = new FileInputStream(readFile);
//        int bytesRead = 0;
//        int count = 0;
//        byte[] buffer = new byte[1024];
//        while((is.read(buffer)) != -1) {
//            os.write(buffer);
//            count++;
//        }
//        is.close();
//        os.close();
////        Files.copy(Paths.get(new String(pathFrom)), Paths.get(baseDir + name), StandardCopyOption.REPLACE_EXISTING);
//        return  destFile.getAbsolutePath();
    }

    public void downloadAsync(String url, String fileName, WebView webview) {
//        Request request = new Request.Builder().url(url).build();
        WebViewHelper webviewHelper = new WebViewHelper(webview);
//        OkHttpClient client = new OkHttpClient();
//        Context context = UTSAndroid.getAppContext();
//        Uri collection = null;
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
//        //        values.put(MediaStore.Downloads.MIME_TYPE, "text/plain");
//        values.put(MediaStore.Downloads.IS_PENDING, 1);
//        Uri item = context.getContentResolver().insert(collection, values);
        webviewHelper.consoleLog("begin fn");
//
        ThreadDemo T1 = new ThreadDemo( "Thread-1");
        T1.start();

        ThreadDemo T2 = new ThreadDemo( "Thread-2");
        T2.start();
//        RequestModule rm = new RequestModule();
//        JSONObject config = new JSONObject();
//        config.put("url", url);
//        config.put("fileName", fileName);
//        config.put("filePath", filePath);
//        UniJSCallback cb1 = new UniJSCallback() {
//            @Override
//            public void invoke(Object o) {
//                webviewHelper.consoleLog("cb1 invoke");
//            }
//
//            @Override
//            public void invokeAndKeepAlive(Object o) {
//                webviewHelper.consoleLog("cb1 invokeAndKeepAlive");
//            }
//        };
//        UniJSCallback cb2 = new UniJSCallback() {
//            @Override
//            public void invoke(Object o) {
//                webviewHelper.consoleLog("cb2 invoke");
//            }
//
//            @Override
//            public void invokeAndKeepAlive(Object o) {
//                webviewHelper.consoleLog("cb2 invokeAndKeepAlive");
//            }
//        };
//
//        rm.startDownload(config, cb1, cb2);
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
////                webviewHelper.consoleLog("onFailure");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
////                webviewHelper.consoleLog("onResponse");
////                System.out.println(response.isSuccessful());
////                if (!response.isSuccessful()) {
////                    throw new IOException("下载失败: " + response.code());
////                }
//            try {
//                ResponseBody body = response.body();
//                if (body == null) {
//                    throw new IOException("响应体为空");
//                }
////                long contentLength = body.contentLength();
////                try (InputStream is = body.byteStream();
////                     FileOutputStream fos = new FileOutputStream(dest)) {
////                    byte[] buffer = new byte[8192];
////                    long total = 0;
////                    int len;
////                    while ((len = is.read(buffer)) != -1) {
////                        fos.write(buffer, 0, len);
////                        total += len;
////                    }
////                    fos.flush();
////                } catch (IOException e) {
////                    callback.onFailure(call, e);
////                }
//                webviewHelper.consoleLog("onResponse");
//            } catch (Exception e) {
//
//                e.printStackTrace();
//            }
//            }
//        });
    }
}

class ThreadDemo extends Thread {
    private Thread t;
    private String threadName;

    ThreadDemo( String name) {
        threadName = name;
        System.out.println("Creating " +  threadName );
    }

    public void run() {
        System.out.println("Running " +  threadName );
        try {
            for(int i = 4; i > 0; i--) {
                System.out.println("Thread: " + threadName + ", " + i);
                // 让线程睡眠一会
                Thread.sleep(50);
            }
        }catch (InterruptedException e) {
            System.out.println("Thread " +  threadName + " interrupted.");
        }
        System.out.println("Thread " +  threadName + " exiting.");
    }

    public void start () {
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}


package com.example.filedownload

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.webkit.WebView
import com.example.common.WebViewHelper
import java.io.IOException
import io.dcloud.uts.UTSAndroid
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

open class FileDownload {
    @Throws(IOException::class)
    public fun downloadByUrl(url: String, fileName: String, webview: WebView, varName: String, globalName: String = "plus") {
        // 执行耗时
        val context = UTSAndroid.getAppContext()
        val looper = context?.mainLooper
        val handler = Handler(looper!!)
        val webviewHelper: WebViewHelper = WebViewHelper(webview)
        // 查询公共下载目录
        var collection: Uri? = null
        webviewHelper.consoleLog("01")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI
        }
        webviewHelper.consoleLog(collection.toString())
        webviewHelper.consoleLog(url)
        webviewHelper.consoleLog(fileName)

        val values = ContentValues()
        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        values.put(MediaStore.Downloads.IS_PENDING, 1)
        values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/CNNP_MES_APP")
        webviewHelper.consoleLog("2")
        webviewHelper.consoleLog(Environment.DIRECTORY_DOWNLOADS)
        val item = context!!.contentResolver.insert(collection!!, values)
        webviewHelper.consoleLog("3.1")
        webviewHelper.consoleLog("3.2-")
        webviewHelper.consoleLog("3.3")
        webviewHelper.consoleLog("4.1")
        var readed:Long=0
        var client = OkHttpClient()
        val request = Request.Builder().url(url).build()
//        val request = Request.Builder().url("https://threejs.org/examples/models/gltf/LittlestTokyo.glb").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                handler.post(object : Runnable {
                    override fun run() {
                        webviewHelper.rejectJsPromise(globalName, varName, e.message)
                    }
                })
            }
            override fun onResponse(call: Call, response: Response) {
                println("on resp")
                response.body()?.byteStream()?.use { input ->
                    var total = response.body()!!.contentLength()
                    readed += input.available()
                    context!!.contentResolver.openOutputStream(item!!).use { os ->
                        if (os != null) {
                            input.copyTo(os)
                        }
                        handler.post(object : Runnable {
                            override fun run() {
                                webviewHelper.webview.evaluateJavascript("($globalName && $globalName['$varName'] && $globalName['$varName'].onProgress && $globalName['$varName'].onProgress({readedSize: ${readed}, totalSize: ${total}}))", null)
                            }
                        })
                    }
                }
                println("The withContext() on the thread: ${Thread.currentThread().name}")
                handler.post(object : Runnable {
                    override fun run() {
                        // 在这里安全地操作 WebView
                        webviewHelper.consoleLog("end")
                        webviewHelper.resolveJsPromise(globalName, varName, response.message())
                    }
                })
                values.clear()
                values.put(MediaStore.Downloads.IS_PENDING, 0)
                context!!.contentResolver.update(item!!, values, null, null)
            }
            fun onSuccess (call: Call, response: Response) {
                if (!response.isSuccessful) {
                    throw IOException("下载失败: ${response.code()}")
                }

            }
        })
    }
}
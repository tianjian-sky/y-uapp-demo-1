@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "SENSELESS_COMPARISON", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
package com.example.sFilePicker
//import io.dcloud.uniapp.*
//import io.dcloud.uniapp.extapi.*
//import io.dcloud.uniapp.framework.*
//import io.dcloud.uniapp.runtime.*
//import io.dcloud.uniapp.vue.*
//import io.dcloud.uniapp.vue.shared.*

import android.app.Activity
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.webkit.WebView
import com.alibaba.fastjson.JSONObject
import com.example.common.WebViewHelper
import com.syczuan.plugin.request.*
import io.dcloud.feature.uniapp.bridge.UniJSCallback
import io.dcloud.uts.JSON
import io.dcloud.uts.JsonNotNull
import io.dcloud.uts.UTSAndroid
import io.dcloud.uts.UTSArray
import io.dcloud.uts.UTSError
import io.dcloud.uts.UTSObject
import io.dcloud.uts.UTSPromise
import io.dcloud.uts._uA
import io.dcloud.uts.compareTo
import io.dcloud.uts.inc
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


open class FileInfo (
    @JsonNotNull
    open var filePath: String,
    @JsonNotNull
    open var fileName: String,
    @JsonNotNull
    open var fileSize: Number,
    @JsonNotNull
    open var mimeType: String,
) : UTSObject()
open class PickFileOptions (
    open var mimeTypes: UTSArray<String>? = null,
    open var multiple: Boolean? = null,
) : UTSObject()
open class ConvertOptions (
    @JsonNotNull
    open var filePath: String,
    open var destName: String? = null,
) : UTSObject()
//fun pickFile(options: PickFileOptions?): UTSPromise<FileInfo> {
//    return UTSPromise<FileInfo>(fun(resolve, reject) {
//        val mimeTypes: UTSArray<String> = if ((options != null && options.mimeTypes != null)) {
//            options.mimeTypes!!
//        } else {
//            _uA(
//                "*/*"
//            )
//        }
//        FilePickerNative.startSinglePick(mimeTypes, fun(uri: String?) {
//            if (uri == null) {
//                reject(UTSError("用户取消选择"))
//                return
//            }
//            val context = UTSAndroid.getAppContext()
//            if (context == null) {
//                reject(UTSError("Context not available"))
//                return
//            }
//            val info = FilePickerNative.getFileInfo(context, uri)
//            if (info == null) {
//                reject(UTSError("获取文件信息失败"))
//                return
//            }
//            val filePath = FilePickerNative.processUriToCache(context, uri, null)
//            if (filePath == null) {
//                reject(UTSError("文件复制到沙盒失败"))
//                return
//            }
//            val name = info["fileName"] as String
//            val size = info["fileSize"] as Number
//            val mime = info["mimeType"] as String
//            resolve(FileInfo(filePath = filePath, fileName = if (name != null) {
//                name
//            } else {
//                "unknown"
//            }
//            , fileSize = if (size != null) {
//                size
//            } else {
//                0
//            }
//            , mimeType = if (mime != null) {
//                mime
//            } else {
//                ""
//            }
//            ))
//        }
//        )
//    }
//    )
//}
fun pickFiles(options: PickFileOptions?): UTSPromise<UTSArray<FileInfo>> {
    return UTSPromise<UTSArray<FileInfo>>(fun(resolve, reject) {
        val mimeTypes: UTSArray<String> = if ((options != null && options.mimeTypes != null)) {
            options.mimeTypes!!
        } else {
            _uA(
                "*/*"
            )
        }
        FilePickerNative.startMultiPick(mimeTypes, fun(jsonResult: String?) {
            if (jsonResult == null) {
                reject(UTSError("用户取消选择"))
                return
            }
            val context = UTSAndroid.getAppContext()
            if (context == null) {
                reject(UTSError("Context not available"))
                return
            }
            val uriList = JSON.parse(jsonResult) as UTSArray<String>
            val results: UTSArray<FileInfo> = _uA()
            run {
                var i: Number = 0
                while(i < uriList.length){
                    val uri = uriList[i]
                    if (uri == null) {
                        i++
                        continue
                    }
                    val info = FilePickerNative.getFileInfo(context, uri)
                    if (info == null) {
                        i++
                        continue
                    }
                    val filePath = FilePickerNative.processUriToCache(context, uri, null, null)
                    if (filePath == null) {
                        i++
                        continue
                    }
                    val name = info["fileName"] as String
                    val size = info["fileSize"] as Number
                    val mime = info["mimeType"] as String
                    results.push(FileInfo(filePath = filePath, fileName = if (name != null) {
                        name
                    } else {
                        "unknown"
                    }
                    , fileSize = if (size != null) {
                        size
                    } else {
                        0
                    }
                    , mimeType = if (mime != null) {
                        mime
                    } else {
                        ""
                    }
                    ))
                    i++
                }
            }
            if (results.length > 0) {
                resolve(results)
            } else {
                reject(UTSError("未获取到文件"))
            }
        }
        )
    }
    )
}
fun convertPath(options: ConvertOptions): UTSPromise<String> {
    return UTSPromise<String>(fun(resolve, reject) {
        val context = UTSAndroid.getAppContext()
        if (context == null) {
            reject(UTSError("Context not available"))
            return
        }
        val destName: String? = if (options.destName != null) {
            options.destName
        } else {
            null
        }
        val filePath = FilePickerNative.processUriToCache(context, options.filePath, destName, null)
        if (filePath == null) {
            reject(UTSError("路径转换失败"))
        } else {
            resolve(filePath)
        }
    }
    )
}
open class SFilePickerHelper {
    public fun pickFile (activity: Activity, webview: WebView, varName: String, globalName: String = "plus"): Any {
        val options = PickFileOptions();
        val webviewHelper: WebViewHelper = WebViewHelper(webview)
        return UTSPromise<FileInfo>(fun(resolve, reject) {
            val mimeTypes: UTSArray<String> = _uA(
                "*/*"
            )
            FilePickerNative.startSinglePick(mimeTypes, fun(uri: String?) {
                webviewHelper.consoleLog("1")
                if (uri == null) {
                    webviewHelper.rejectJsPromise(globalName, varName, "用户取消选择")
//                    reject(UTSError("用户取消选择"))
                    return
                }
                val context = UTSAndroid.getAppContext()
                webviewHelper.consoleLog("2")
                if (context == null) {
                    webviewHelper.rejectJsPromise(globalName, varName, "Context not available")
//                    reject(UTSError("Context not available"))
                    return
                }
                webviewHelper.consoleLog("3")
                val info = FilePickerNative.getFileInfo(context, uri)
                if (info == null) {
                    webviewHelper.rejectJsPromise(globalName, varName, "获取文件信息失败")
//                    reject(UTSError("获取文件信息失败"))
                    return
                }
                webviewHelper.consoleLog("4")
                val filePath = FilePickerNative.processUriToCache(context, uri, null, webviewHelper)
                if (filePath == null) {
                    webviewHelper.rejectJsPromise(globalName, varName, "文件复制到沙盒失败")
//                    reject(UTSError("文件复制到沙盒失败"))
                    return
                }
                val name = info["fileName"] as String
                val size = info["fileSize"] as Number
                val mime = info["mimeType"] as String
                webviewHelper.consoleLog("5")
                webviewHelper.resolveJsPromise(globalName, varName, "{filePath: \"" + filePath + "\"}");
            }, activity);
        })
        return 1;
    };

    fun _downloadFile(url: String?, fileName: String?, webview: WebView) {



    }

    @Throws(IOException::class)
    public fun testDownload(url: String, fileName: String, webview: WebView) {
//        ThreadDemo(url, fileName, webview).start()
//        UTSAndroid.getDispatcher("io").async( {
            // 执行耗时任
        val context = UTSAndroid.getAppContext()
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
        //        values.put(MediaStore.Downloads.MIME_TYPE, "text/plain");
        values.put(MediaStore.Downloads.IS_PENDING, 1)
        webviewHelper.consoleLog("2")
        val item = context!!.contentResolver.insert(collection!!, values)
        webviewHelper.consoleLog("3.1")
        webviewHelper.consoleLog("3.2-")
                // Starts a coroutine inside the scope with CoroutineScope.launch()
        webviewHelper.consoleLog("3.3")
            webviewHelper.consoleLog("4.1")
//        val client = OkHttpClient()
//        val request = Request.Builder().url(url).get().build()
//        client.newCall(request).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    webviewHelper.consoleLog("on onFailure")
//                }
//                override fun onResponse(call: Call, response: Response) {
//                    TODO("Not yet implemented")
//                    webviewHelper.consoleLog("on resp")
//                }
//                fun onSuccess (call: Call, response: Response) {
//                    webviewHelper.consoleLog("on onSuccess")
//                    if (!response.isSuccessful) {
//                        throw IOException("下载失败: ${response.code()}")
//                    }
//                    response.body()?.byteStream()?.use { input ->
//                        context!!.contentResolver.openOutputStream(item!!).use { os ->
//                            if (os != null) {
//                                input.copyTo(os)
//                            }
//                            webviewHelper.consoleLog("copy end")
//                        }
//                    }
//                    println("The withContext() on the thread: ${Thread.currentThread().name}")
//                    webviewHelper.consoleLog("end")
//                    values.clear()
//                    values.put(MediaStore.Downloads.IS_PENDING, 0)
//                    context!!.contentResolver.update(item!!, values, null, null)
//                }
//        })
            val rm = RequestModule()
            val config = JSONObject()
            config.put("url", url)
            config.put("fileName", fileName)
            config.put("filePath", item)
            val cb1: UniJSCallback = object : UniJSCallback {
                override fun invoke(o: Any?) {
                    webviewHelper.consoleLog("cb1 invoke")
                }

                override fun invokeAndKeepAlive(o: Any?) {
                    webviewHelper.consoleLog("cb1 invokeAndKeepAlive")
                }
            }
            val cb2: UniJSCallback = object : UniJSCallback {
                override fun invoke(o: Any?) {
                    webviewHelper.consoleLog("cb2 invoke")
                }

                override fun invokeAndKeepAlive(o: Any?) {
                    webviewHelper.consoleLog("cb2 invokeAndKeepAlive")
                }
            }

            rm.startDownload(config, cb1, cb2)
            println("任务执行")
//        webviewHelper.consoleLog("3.4")
//        },null)
    }
}

internal class ThreadDemo(val url: String, val fileName: String, webview: WebView) : Thread() {
    private var t: Thread? = null
    val webviewHelper = WebViewHelper(webview)
    init {
        webviewHelper.consoleLog("init")
    }

    override fun run() {
        val context = UTSAndroid.getAppContext()
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
        //        values.put(MediaStore.Downloads.MIME_TYPE, "text/plain");
        values.put(MediaStore.Downloads.IS_PENDING, 1)
        webviewHelper.consoleLog("2")
        val item = context!!.contentResolver.insert(collection!!, values)
        webviewHelper.consoleLog("3.1")
        webviewHelper.consoleLog("3.2-")
        // Starts a coroutine inside the scope with CoroutineScope.launch()
        webviewHelper.consoleLog("3.3")
        webviewHelper.consoleLog("4.1")
        val client = OkHttpClient()
        val request = Request.Builder().url(url).get().build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                webviewHelper.consoleLog("on onFailure")
            }
            override fun onResponse(call: Call, response: Response) {
                TODO("Not yet implemented")
                webviewHelper.consoleLog("on resp")
            }
            fun onSuccess (call: Call, response: Response) {
                webviewHelper.consoleLog("on onSuccess")
                if (!response.isSuccessful) {
                    throw IOException("下载失败: ${response.code()}")
                }
                response.body()?.byteStream()?.use { input ->
                    context!!.contentResolver.openOutputStream(item!!).use { os ->
                        if (os != null) {
                            input.copyTo(os)
                        }
                        webviewHelper.consoleLog("copy end")
                    }
                }
                println("The withContext() on the thread: ${Thread.currentThread().name}")
                webviewHelper.consoleLog("end")
                values.clear()
                values.put(MediaStore.Downloads.IS_PENDING, 0)
                context!!.contentResolver.update(item!!, values, null, null)
            }
        })
        println("任务执行")
        webviewHelper.consoleLog("3.4")
    }

    override fun start() {
        println("Starting " + url)
        webviewHelper.consoleLog("00")
        if (t == null) {
            t = Thread(this, url)
            t!!.start()
        }
    }
}
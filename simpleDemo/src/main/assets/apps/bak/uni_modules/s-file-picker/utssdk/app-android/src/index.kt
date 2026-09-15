@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "SENSELESS_COMPARISON", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
package uts.sdk.modules.sFilePicker
import io.dcloud.uniapp.*
import io.dcloud.uniapp.extapi.*
import io.dcloud.uniapp.framework.*
import io.dcloud.uniapp.runtime.*
import io.dcloud.uniapp.vue.*
import io.dcloud.uniapp.vue.shared.*
import io.dcloud.uts.*
import io.dcloud.uts.Map
import io.dcloud.uts.Set
import io.dcloud.uts.UTSAndroid
import kotlin.properties.Delegates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
fun pickFile(options: PickFileOptions?): UTSPromise<FileInfo> {
    return UTSPromise<FileInfo>(fun(resolve, reject) {
        val mimeTypes: UTSArray<String> = if ((options != null && options.mimeTypes != null)) {
            options.mimeTypes!!
        } else {
            _uA(
                "*/*"
            )
        }
        FilePickerNative.startSinglePick(mimeTypes, fun(uri: String?) {
            if (uri == null) {
                reject(UTSError("用户取消选择"))
                return
            }
            val context = UTSAndroid.getAppContext()
            if (context == null) {
                reject(UTSError("Context not available"))
                return
            }
            val info = FilePickerNative.getFileInfo(context, uri)
            if (info == null) {
                reject(UTSError("获取文件信息失败"))
                return
            }
            val filePath = FilePickerNative.processUriToCache(context, uri, null)
            if (filePath == null) {
                reject(UTSError("文件复制到沙盒失败"))
                return
            }
            val name = info["fileName"] as String
            val size = info["fileSize"] as Number
            val mime = info["mimeType"] as String
            resolve(FileInfo(filePath = filePath, fileName = if (name != null) {
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
        }
        )
    }
    )
}
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
                    val filePath = FilePickerNative.processUriToCache(context, uri, null)
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
        val filePath = FilePickerNative.processUriToCache(context, options.filePath, destName)
        if (filePath == null) {
            reject(UTSError("路径转换失败"))
        } else {
            resolve(filePath)
        }
    }
    )
}

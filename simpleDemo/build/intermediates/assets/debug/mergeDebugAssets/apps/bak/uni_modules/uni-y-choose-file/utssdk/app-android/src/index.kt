@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "SENSELESS_COMPARISON", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
package uts.sdk.modules.uniYChooseFile
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
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.properties.Delegates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.nio.file.*
val getYChooseFile = fun(readPath: String, folderPath: String, fileName: String) {
    println(readPath)
    println(folderPath)
    println(fileName)
    val readFile = File(readPath)
    val canRead = readFile.canRead()
    val canWrite = readFile.canWrite()
    val canExecute = readFile.canExecute()
    println("canRead" + canRead)
    println("canRead" + canWrite)
    println("canRead" + canExecute)
    var kIs = FileInputStream(readFile)
    val writeFolderPath = folderPath
    val folder = File(writeFolderPath)
    val flg = folder.mkdirs()
    val file = File(writeFolderPath + "/" + fileName)
    val flg2 = file.createNewFile()
    val os = FileOutputStream(file)
    var count: Number = 0
    val bufferSize: Int = 1024
    var buffer = ByteArray(bufferSize)
    while(true){
        var bytesRead = kIs.read(buffer)
        if (bytesRead == -1) {
            break
        } else {
            os.write(buffer)
            count++
        }
    }
    kIs.close()
    os.close()
    println("复制成功")
}

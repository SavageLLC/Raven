package com.savagellc.raven.utils

import java.io.File
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.file.*


enum class FileReturnResult {
    ERROR,
    SUCCESS,
    IS_DIR,
    NOT_FOUND
}


fun deleteDirectoryRecursion(path: Path) {
    if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
        Files.newDirectoryStream(path).use { entries ->
            for (entry in entries) {
                deleteDirectoryRecursion(entry)
            }
        }
    }
    Files.delete(path)
}

fun writeFile(data: ByteArray, path: String, append: Boolean = false, createIfNotExists: Boolean = false) =
    writeFile(data, File(path), append, createIfNotExists)

fun writeFile(data: ByteArray, path: String) = writeFile(data, File(path))

fun readFile(path: String) = readFile(File(path))
fun readFile(file: File): Pair<FileReturnResult, String> {

    if (!file.exists()) return Pair(FileReturnResult.NOT_FOUND, "")

    if (file.isDirectory) return Pair(FileReturnResult.IS_DIR, "")
    return try {


        val array = Files.readAllBytes(file.toPath())
        Pair(FileReturnResult.SUCCESS, String(array))
    } catch (e: Exception) {
        e.printStackTrace()
        Pair(FileReturnResult.ERROR, "")

    }
}
fun readFileBinary(file: File): Pair<FileReturnResult, ByteArray> {

    if (!file.exists()) return Pair(FileReturnResult.NOT_FOUND, ByteArray(0))

    if (file.isDirectory) return Pair(FileReturnResult.IS_DIR, ByteArray(0))
    return try {


        val array = Files.readAllBytes(file.toPath())
        Pair(FileReturnResult.SUCCESS, array)
    } catch (e: Exception) {
        e.printStackTrace()
        Pair(FileReturnResult.ERROR, ByteArray(0))

    }
}


fun writeFile(
    data: ByteArray,
    file: File,
    append: Boolean = false,
    createIfNotExists: Boolean = false
): Pair<FileReturnResult, String> {

    if (!createIfNotExists && !file.exists())
        return Pair(FileReturnResult.NOT_FOUND, "")

    if (createIfNotExists && !file.exists()) {
        val created = file.createNewFile()
        if (!created) return Pair(FileReturnResult.ERROR, "")
    }

    return try {
        if (append)
            Files.write(Paths.get(file.absolutePath), data, StandardOpenOption.APPEND)
        else
            Files.write(Paths.get(file.absolutePath), data, StandardOpenOption.TRUNCATE_EXISTING)
        Pair(FileReturnResult.SUCCESS, "")
    } catch (e: Exception) {
        e.printStackTrace()
        Pair(FileReturnResult.ERROR, e.message!!)
    }
}
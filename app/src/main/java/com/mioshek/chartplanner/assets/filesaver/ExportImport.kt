package com.mioshek.chartplanner.assets.filesaver

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.mioshek.chartplanner.views.settings.CustomOption
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.InvalidObjectException

class ExportImport : Activity() {
    lateinit var data: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = intent.extras?.get("Data") as String

        when(intent.extras?.get("Option")){
            CustomOption.EXPORT -> {
                createFile(this)
            }

            CustomOption.IMPORT -> {
                openFile(this)
            }

            else -> {
                throw InvalidObjectException("Invalid Option Passed")
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == 1  && resultCode == RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            resultData?.data?.also { uri ->
                insertDataIntoFile(uri, applicationContext, data)
            }
            finish()
        }
        else if (requestCode == 2 && resultCode == RESULT_OK){
            resultData?.data?.also { uri ->
                Log.d("Data",readData(uri, applicationContext))
            }
            finish()
        }
    }
}

private fun createFile(context: Context) {
    val activity = context as Activity
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        setDataAndType(Environment.getExternalStorageDirectory().path.toUri(), "application/json")
        addCategory(Intent.CATEGORY_OPENABLE)
        putExtra(Intent.EXTRA_TITLE, "ExportHabits.json")
    }
    ActivityCompat.startActivityForResult(activity, intent, 1, null)
}

private fun insertDataIntoFile(uri: Uri, context: Context, data: String){
    try {
        context.contentResolver.openFileDescriptor(uri, "w")?.use {
            FileOutputStream(it.fileDescriptor).use {
                it.write(
                    data.toByteArray()
                )
            }
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

private fun readData(uri: Uri, context: Context,): String {
    val contentResolver = context.contentResolver
    val stringBuilder = StringBuilder()
    contentResolver.openInputStream(uri)?.use { inputStream ->
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            var line: String? = reader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = reader.readLine()
            }
        }
    }
    return stringBuilder.toString()
}

private fun openFile(context: Context) {
    val activity = context as Activity
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/json"

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
    }

    ActivityCompat.startActivityForResult(activity, intent, 2, Bundle())
}
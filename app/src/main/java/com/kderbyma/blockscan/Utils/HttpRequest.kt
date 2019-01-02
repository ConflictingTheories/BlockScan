package com.kderbyma.blockscan.Utils

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by kderbyma on 07/10/17.
 */

class HttpRequest : AsyncTask<String, Void, String> {

    // Performs GET Request (Called by execute())
    var REQUEST_METHOD = "GET"
    var READ_TIMEOUT = 15000
    var CONNECTION_TIMEOUT = 15000
    var BASE_URL = ""

    // if no method -- do nothing
    internal constructor()

    // setup the method
    internal constructor(METHOD: String) {
        this.REQUEST_METHOD = METHOD
    }

    // setup the method
    constructor(METHOD: String, BASE: String) {
        this.REQUEST_METHOD = METHOD
        this.BASE_URL = BASE
    }

    // Run Command
    override fun doInBackground(vararg params: String): String? {

        val stringUrl = this.BASE_URL + params[0]
        var result: String?
        var inputLine: String

        try {
            //Create a URL object holding our url
            val myUrl = URL(stringUrl)
            Log.i("RUNNING::", stringUrl)

            //Create a connection
            val connection = myUrl.openConnection() as HttpURLConnection
            //Set methods and timeouts
            connection.requestMethod = REQUEST_METHOD
            connection.readTimeout = READ_TIMEOUT
            connection.connectTimeout = CONNECTION_TIMEOUT
            // Make Connection
            connection.connect()

            //Create a new InputStreamReader
            val streamReader = InputStreamReader(connection.inputStream)
            //Create a new buffered reader and String Builder
            val reader = BufferedReader(streamReader)
            val stringBuilder = StringBuilder()
            //Check if the line we are reading is not null
            while (true) {
                inputLine = reader.readLine() ?: break;
                Log.i("RE:", inputLine)
                stringBuilder.append(inputLine)
            }
            //Close our InputStream and Buffered reader
            reader.close()
            streamReader.close()
            //Set our result equal to our stringBuilder
            result = stringBuilder.toString()

            Log.i("RUNNING::", result)
        } catch (e: IOException) {
            e.printStackTrace()
            result = null
        }

        return result
    }

}

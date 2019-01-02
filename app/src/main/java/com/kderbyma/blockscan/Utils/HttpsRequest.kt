package com.kderbyma.blockscan.Utils

import android.os.AsyncTask
import android.util.Log
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import java.io.*

/**
 * Created by kderbyma on 07/10/17.
 */

class CallAPI : AsyncTask<String, String, String> {

    // DEFAULTS
    var REQUEST_METHOD = "GET"
    var READ_TIMEOUT = 15000
    var CONNECTION_TIMEOUT = 15000
    var BASE_URL = ""
    var DATA = ""

    constructor(METHOD: String, BASE: String, DATA: String) {
            this.REQUEST_METHOD = METHOD
            this.BASE_URL = BASE
            this.DATA = DATA;
    }

    override fun doInBackground(vararg params: String) :String?{
        val urlString = this.BASE_URL // URL to call
        val data = this.DATA //data to post
        var out: OutputStream? = null
        var inputLine: String
        var result: String?

        Log.i("HERE",this.DATA)

        try {
            val url = URL(urlString)
            val urlConnection = url.openConnection() as HttpsURLConnection

            urlConnection.readTimeout = READ_TIMEOUT
            urlConnection.connectTimeout = CONNECTION_TIMEOUT

            urlConnection.setRequestMethod(REQUEST_METHOD);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            out = BufferedOutputStream(urlConnection.getOutputStream()) as OutputStream?

            val writer = BufferedWriter(OutputStreamWriter(out, "UTF-8"))
            writer.write(data)
            writer.flush()
            writer.close()
            out?.close()

            urlConnection.connect()

            //Create a new InputStreamReader
            val streamReader = InputStreamReader(urlConnection.inputStream)
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

        } catch (e: Exception) {
            println(e.message)
            result = "{\"msg\":\"Error\"}";
        }
        Log.i("RES:",result)

        return result
    }


}//set context variables if required
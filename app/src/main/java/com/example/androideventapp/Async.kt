package com.example.androideventapp

import android.os.AsyncTask
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


private val client = OkHttpClient()



class Async: AsyncTask<Request, Int, Response>() {


    override fun doInBackground(vararg p0: Request?): Response? {

        var endResponse: Response? = null;

        p0[0]?.let {
            client.newCall(it).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                endResponse = response;
                //println(response.body!!.string())
            }
        }

        return endResponse;
    }

    override fun onPostExecute(result: Response?) {
        super.onPostExecute(result)
    }

}
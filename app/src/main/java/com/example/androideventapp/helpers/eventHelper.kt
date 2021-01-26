package com.example.androideventapp.helpers

import android.content.Context
import android.content.SharedPreferences
import com.example.androideventapp.R
import com.example.androideventapp.models.Event
import com.example.androideventapp.models.EventType
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException


fun fetchTypes(context: Context,flag: String?, then: ((MutableList<EventType>) -> Unit)){


    var url: String = context.getString(R.string.backEndHost) + "events/getTypes"

    if(flag == "false"){
        url+= "/?Activo=false"
    }
    if(flag == "true"){
        url += "/?Activo=true"
    }

    val client = OkHttpClient()
    var sharedPrefs : SharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE) ?: return
    var token: String = sharedPrefs.getString("token","token")

    var request = Request.Builder().url(url).header(
        "Authorization",
        token
    ).build()


    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val body = response.body!!.string()

                //println(body)
                val gson = GsonBuilder().create()

                val Tipos = gson.fromJson(
                    body,
                    Array<EventType>::class.java
                ).toMutableList()

                then(Tipos)

            }
        }
    })
}

fun createEvent(context: Context, latitude: String, longitude: String, eventType: Int, then: ((String) -> Unit))  {

    var url: String = context.getString(R.string.backEndHost) + "events/userCreateEvent/"

    val client = OkHttpClient()
    var sharedPrefs : SharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE) ?: return
    var token: String = sharedPrefs.getString("token","token")

    var body: RequestBody = FormBody.Builder()
        .add("coordsx", latitude)
        .add("coordsy",longitude)
        .add("TipoEvento",eventType.toString())
        .add("Activo","True")
        .add("Usuario","2")
        .build()

    var request = Request.Builder().url(url).header(
        "Authorization",
        token
    ).post(body).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val body = response.body!!.string()

                then("success")



            }
        }
    })




}

fun changeTypeStatus(context: Context,id: Int, status: String, then: ((EventType) -> Unit)){



    var url: String = context.getString(R.string.backEndHost) + "events/update/"+id


    val client = OkHttpClient()
    var sharedPrefs : SharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE) ?: return
    var token: String = sharedPrefs.getString("token","token")

    var body: RequestBody = FormBody.Builder()
        .add("Activo",status).build()


    var request = Request.Builder().url(url).header(
        "Authorization",
        token
    ).patch(body).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val body = response.body!!.string()


                val gson = GsonBuilder().create()

                val newType = gson.fromJson(
                    body,
                    EventType::class.java
                )

                then(newType)

            }
        }
    })
}

fun createNewType(context: Context, color: String, name: String, then: ((EventType) -> Unit)){
    var url: String = context.getString(R.string.backEndHost) + "events/tiposEvent/"


    val client = OkHttpClient()
    var sharedPrefs : SharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE) ?: return
    var token: String = sharedPrefs.getString("token","token")

    var body: RequestBody = FormBody.Builder()
        .add("nombre",name)
        .add("color",color)
        .add("Activo","true").build()


    var request = Request.Builder().url(url).header(
        "Authorization",
        token
    ).post(body).build()


    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val body = response.body!!.string()

                val gson = GsonBuilder().create()

                val newType = gson.fromJson(
                    body,
                    EventType::class.java
                )

                then(newType)


            }
        }
    })
}

fun eventDelete(context: Context,id: Int, then: ((String) -> Unit)){

    var url: String = context.getString(R.string.backEndHost) + "events/deleteEvent/"+id


    val client = OkHttpClient()
    var sharedPrefs : SharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE) ?: return
    var token: String = sharedPrefs.getString("token","token")




    var request = Request.Builder().url(url).header(
        "Authorization",
        token
    ).delete().build()


    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                println(response)
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                if(response.code == 204){
                    then("Evento eliminado con exito")
                }

            }
        }
    })
}
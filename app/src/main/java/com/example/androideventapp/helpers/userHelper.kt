package com.example.androideventapp.helpers

import android.content.Context
import android.content.SharedPreferences
import android.widget.CheckBox
import com.example.androideventapp.R
import com.example.androideventapp.models.SimpleUser
import com.example.androideventapp.models.User
import com.google.android.gms.common.api.Api
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException





    fun fetchUserforUpdate(context: Context, then: ((User) -> Unit)) {

        var url: String = context.getString(R.string.backEndHost) + "usuarios/me/"
        val client = OkHttpClient()
        var sharedPrefs : SharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE) ?: return
        var token: String = sharedPrefs.getString("token","token")




        var request = Request.Builder().url(url).header(
            "Authorization",token
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

                    val Usuario = gson.fromJson(body, User::class.java)


                    then(Usuario)

                }
            }


        })
    }

    fun updateUsersPassword(context: Context, password: String) {
        var url: String = context.getString(R.string.backEndHost) + "usuarios/me/"
        val client = OkHttpClient()
        var sharedPrefs : SharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE) ?: return
        var token: String = sharedPrefs.getString("token","token")

        var body: RequestBody = FormBody.Builder()
            .add("password", password)
            .build()

        var request = Request.Builder().url(url)
            .header(
                "Authorization",
                token
            )
            .patch(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")



                }
            }


        })


    }

    fun updateUserGeneralInfo(context: Context,email: String, phone: String, nombre: String, apellido: String, then: ((User) -> Unit)) {

        var url: String = context.getString(R.string.backEndHost) + "usuarios/me/"
        val client = OkHttpClient()
        var sharedPrefs : SharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE) ?: return
        var token: String = sharedPrefs.getString("token","token")


        var body: RequestBody = FormBody.Builder()
            .add("email", email)
            .add("telefono",phone)
            .add("nombre",nombre)
            .add("apellido",apellido)
            .build()

        var request = Request.Builder().url(url)
            .header(
                "Authorization",
                token
            )
            .patch(body).build()

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

                    val Usuario = gson.fromJson(body, User::class.java)


                    then(Usuario)


                }
            }


        })


    }
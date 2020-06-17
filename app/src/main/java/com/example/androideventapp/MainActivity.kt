package com.example.androideventapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private lateinit var new: JSONObject



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun userLogin(view: View) {
        var emailEditText: EditText = findViewById<EditText>(R.id.loginEmail)
        var passEditText: EditText = findViewById<EditText>(R.id.loginPassword)


        //Log.i("message","idkasdsasad ")



        if(emailValidate(emailEditText.text.toString())){

            makeRequest(emailEditText.text.toString(),passEditText.text.toString())

        }

    }

    private fun emailValidate(emailText: String): Boolean {



        return if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            true;
        } else{
            Toast.makeText(this,"Porfavor ingrese un correo electronico valido",Toast.LENGTH_SHORT).show();
            //Log.i("message",emailText)
            false;
        }


    }

    private fun makeRequest(emailEditText: String, passEditText: String){

        var url: String = getString(R.string.backEndHost) + "usuarios/token/"

        //var userInfo = JSONObject("{email:"+emailEditText.text.toString()+",password:"+passEditText.text.toString()+"}")
        //TODO find a way to this in a less convoluted way

        var body: RequestBody = FormBody.Builder()
            .add("username",emailEditText)
            .add("password",passEditText)
            .build()

        val request = Request.Builder().url(url).post(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    //if (!response.isSuccessful) throw IOException("Unexpected code $response")


                    if(response.isSuccessful){
                        new = JSONObject(response.body!!.string())

                        Log.i("asdsad","idk man your response worked")
                        val preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                        preferences.edit().putString("token", new.get("token").toString()).apply()
                        preferences.edit().putString("admin",new.get("admin").toString()).apply()

                        if(new.get("admin").toString().toBoolean()){
                            runOnUiThread{
                                var intent = Intent(this@MainActivity,AdminMenu::class.java)
                                startActivity(intent)
                            }

                        }
                        else{
                            runOnUiThread{
                                var intent = Intent(this@MainActivity,UserMenu::class.java)
                                startActivity(intent)
                            }
                        }

                    }else if(response.code == 403){

                        new = JSONObject(response.body!!.string())

                        runOnUiThread {
                            Toast.makeText(this@MainActivity,new.get("msg").toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        runOnUiThread {
                            Toast.makeText(this@MainActivity,"Problema con el servidor!!!",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }




}


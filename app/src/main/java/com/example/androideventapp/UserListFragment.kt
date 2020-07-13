package com.example.androideventapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androideventapp.helpers.*
import com.example.androideventapp.models.User
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.user_list_fragment.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class UserListFragment : Fragment() {
    //private var userList = JsonArray()
    lateinit var textView: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.user_list_fragment, container, false)

    }



    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usersRecyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)




        fetchJson()

        addUserButton.setOnClickListener {
            var popupView: View = layoutInflater.inflate(R.layout.create_user_window, null)

            var width: Int = 900;
            var height: Int = LinearLayout.LayoutParams.WRAP_CONTENT;
            var focusable: Boolean = true;
            var popup = PopupWindow(popupView,width,height,focusable)

            popup.showAtLocation(view, Gravity.CENTER,0,100)

            var inputButton: Button = popupView.findViewById<Button>(R.id.buttonInput)
            inputButton.setText("Crear")

            inputButton.setOnClickListener {

                var editEmail: EditText = popupView.findViewById<EditText>(R.id.editTextEmail)
                var editPhone: EditText = popupView.findViewById<EditText>(R.id.editTextTelephone)
                var editNombre: EditText = popupView.findViewById<EditText>(R.id.editTextNombre)
                var editApellido: EditText = popupView.findViewById<EditText>(R.id.editTextApellido)
                var editPassword: EditText =
                    popupView.findViewById<EditText>(R.id.editTextPassword1)
                var editPassword2: EditText =
                    popupView.findViewById<EditText>(R.id.editTextPassword2)

                if (validateEmail(editEmail) and
                    validatePhone(editPhone) and
                    validateNoEmptySpaces(editNombre) and
                    validateNoEmptySpaces(editApellido) and
                    validateIsEmpty(editPassword) and
                    validateIsEmpty(editPassword2)
                ) {
                    if (editPassword.text.toString() != editPassword2.text.toString()) {

                        customDialogue(activity!!, "Contrasenas no son iguales", "alert")

                    } else {

                        var url: String = getString(R.string.backEndHost) + "usuarios/create/"
                        val client = OkHttpClient()
                        val myString: String

                        var body: RequestBody = FormBody.Builder()
                            .add("email", editEmail.text.toString())
                            .add("telefono", "+" + editPhone.text.toString())
                            .add("apellido", editApellido.text.toString())
                            .add(
                                "is_admin",
                                popupView.findViewById<CheckBox>(R.id.adminBox).isChecked.toString()
                            )
                            .add("password", editPassword.text.toString())
                            .add("nombre", editNombre.text.toString())
                            .build()

                        var request = Request.Builder().url(url)
                            .header(
                                "Authorization",
                                "Token b29d64a6178158d6a7fa0b2d5f49e109d28358e2"
                            )
                            .post(body).build()

                        client.newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                e.printStackTrace()
                            }

                            override fun onResponse(call: Call, response: Response) {
                                response.use {
                                    var new = JSONObject(response.body!!.string())


                                    if (response.isSuccessful) {

                                        activity?.runOnUiThread {

                                            var group: ViewGroup =
                                                popupView.findViewById<ViewGroup>(R.id.mainLayout);

                                            clearChildren(group)


                                            usersRecyclerView.adapter?.notifyDataSetChanged()


                                            customDialogue(
                                                activity!!,
                                                "Usuario creado exitosamente.",
                                                "success"
                                            )
                                        }
                                    } else if (new.has("detail")) {

                                        activity?.runOnUiThread {


                                            customDialogue(
                                                activity!!,
                                                "Acceso invalido porfavor reinicia la applicacion",
                                                "error"
                                            )
                                        }

                                    } else {
                                        var returnString: String? = ""
                                        if (new.has("email")) {
                                            returnString += new.getJSONArray("email")[0].toString() + "\n"
                                        }
                                        if (new.has("telefono")) {
                                            returnString += new.getJSONArray("telefono")[0].toString() + "\n"
                                        }

                                        activity?.runOnUiThread {
                                            println(new)
                                            if (returnString != null) {
                                                customDialogue(activity!!, returnString, "alert")
                                            }
                                        }
                                    }
                                }
                            }
                        })
                    }
                }
            }

            popupView.setOnTouchListener { _, _ ->
                popup.dismiss()
                true
            }
        }
    }

    interface GetLastIdCallback {
        fun lastId(id: String?)
    }

    private fun fetchJson() {
        var url: String = getString(R.string.backEndHost) + "usuarios/all/"
        val client = OkHttpClient()
        val myString: String
        //Token for testing purposes 
        var request = Request.Builder().url(url).header("Authorization","Token b29d64a6178158d6a7fa0b2d5f49e109d28358e2").build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response){
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val body = response.body!!.string()

                    val gson = GsonBuilder().create()
                    val Usuarios: Array<User> = gson.fromJson(
                        body,
                        Array<User>::class.java
                    )


                    activity?.runOnUiThread {
                        usersRecyclerView.adapter = UsersListAdapter(Usuarios)
                    }

                }
            }
        })

    }


}




class HomeFeed(val users: List<User>)


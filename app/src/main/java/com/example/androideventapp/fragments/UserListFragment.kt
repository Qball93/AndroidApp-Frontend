package com.example.androideventapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
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
import kotlin.properties.Delegates


class UserListFragment : Fragment() {
    //private var userList = JsonArray()
    lateinit var textView: TextView
    private var updatePosition by Delegates.notNull<Int>()
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var Token: String


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

            popupView.findViewById<CheckBox>(R.id.is_active).visibility = View.GONE

            inputButton.setOnClickListener {

                var editEmail: EditText = popupView.findViewById<EditText>(R.id.editTextEmail)
                var editPhone: EditText = popupView.findViewById<EditText>(R.id.editTextTelephone)
                var editNombre: EditText = popupView.findViewById<EditText>(R.id.editTextNombre)
                var editApellido: EditText = popupView.findViewById<EditText>(R.id.editTextApellido)
                var editPassword: EditText = popupView.findViewById<EditText>(R.id.editTextPassword1)




                if (validateEmail(editEmail) and
                    validatePhone(editPhone) and
                    validateNoEmptySpaces(editNombre) and
                    validateNoEmptySpaces(editApellido) and
                    validateIsEmpty(editPassword)
                ) {


                        var url: String = getString(R.string.backEndHost) + "usuarios/create/"
                        val client = OkHttpClient()
                        val myString: String

                        println("updated")
                        sharedPrefs = activity?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE) ?: return@setOnClickListener
                        Token = sharedPrefs.getString("token", "token")

                        
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
                            .add("is_active","true")
                            .build()

                        var request = Request.Builder().url(url)
                            .header(
                                "Authorization",
                                Token
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

                                            var tempUser = JSONObject(new.toString())

                                            tempUser.put("events",0)
                                                .put("last_login",null)
                                                .put("expanded", false)
                                                .put("is_active",true)

                                            val gson = GsonBuilder().create()
                                            val newUser: User = gson.fromJson(tempUser.toString(),User::class.java)

                                            (usersRecyclerView.adapter as UsersListAdapter).addItem(newUser)
                                            (usersRecyclerView.adapter as UsersListAdapter).notifyItemInserted(
                                                usersRecyclerView.adapter!!.itemCount)

                                            customDialogue(
                                                activity!!,
                                                "Usuario creado exitosamente.",

                                                "success"
                                            )
                                        }
                                    }
                                    else if (new.has("detail")) {


                                        activity?.runOnUiThread {

                                            customDialogue(
                                                activity!!,
                                                "Acceso invalido porfavor reinicia la applicacion",
                                                "error"
                                            )
                                        }
                                    }
                                    else {
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
        sharedPrefs = activity?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE) ?: return
        Token = sharedPrefs.getString("token","token")


        var request = Request.Builder().url(url).header("Authorization",Token).build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response){
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val body = response.body!!.string()

                    val gson = GsonBuilder().create()
                    /*val Usuarios: MutableList<User> = gson.fromJson(
                        body,
                        UserFeed::class.java
                    )*/
                    val Usuarios = gson.fromJson(body, Array<User>::class.java).toMutableList()


                    activity?.runOnUiThread {

                        usersRecyclerView.adapter = UsersListAdapter(Usuarios, this@UserListFragment , object : UsersListAdapter.ItemClickListener{
                            override fun itemClick(user: User, position: Int) {
                                sharedPrefs = activity?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE) ?: return

                                println(user)
                                var currentUserEmail :String = sharedPrefs.getString("emailAdmin","emailAdmin")

                                var popupView: View = layoutInflater.inflate(R.layout.create_user_window, null)

                                var width: Int = 900;
                                var height: Int = LinearLayout.LayoutParams.WRAP_CONTENT;
                                var focusable: Boolean = true;
                                var popup = PopupWindow(popupView,width,height,focusable)

                                popup.showAtLocation(view, Gravity.CENTER,0,100)

                                var inputButton: Button = popupView.findViewById<Button>(R.id.buttonInput)
                                inputButton.text = "Editar"

                                var editNombre : EditText = popupView.findViewById<EditText>(R.id.editTextNombre)
                                editNombre.setText(user.nombre)

                                var editApellido : EditText = popupView.findViewById<EditText>(R.id.editTextApellido)
                                editApellido.setText(user.apellido)

                                var editEmail: EditText = popupView.findViewById<EditText>(R.id.editTextEmail)
                                editEmail.setText(user.email)

                                var editPhone: EditText = popupView.findViewById<EditText>(R.id.editTextTelephone)
                                editPhone.setText(user.telefono.drop(1))

                                var editPassword: EditText = popupView.findViewById<EditText>(R.id.editTextPassword1)
                                editPassword.setText("samepassiuj")



                                var adminCheck : CheckBox = popupView.findViewById<CheckBox>(R.id.adminBox)
                                var activeCheck : CheckBox = popupView.findViewById<CheckBox>(R.id.is_active)
                                activeCheck.isChecked = user.is_active
                                adminCheck.isChecked = user.is_admin

                                if(user.email == currentUserEmail){
                                    adminCheck.isClickable = false
                                    activeCheck.isClickable = false
                                }

                                inputButton.setOnClickListener{
                                    if (validateEmail(editEmail) and
                                        validatePhone(editPhone) and
                                        validateNoEmptySpaces(editNombre) and
                                        validateNoEmptySpaces(editApellido) and
                                        validateIsEmpty(editPassword)
                                    ) {
                                       updateUser(editEmail.text.toString(),editPassword.text.toString(),editNombre.text.toString(),adminCheck.isChecked,
                                                activeCheck.isChecked, editPhone.text.toString(), editApellido.text.toString(),user.id, popupView, position){
                                                    res ->     (usersRecyclerView.adapter as UsersListAdapter).replaceItem(res,updatePosition)
                                                activity?.runOnUiThread{
                                                    popup.dismiss()
                                                    println("change made")
                                                    customDialogue(activity!!,"Usuario Editado","success")
                                                    (usersRecyclerView.adapter as UsersListAdapter).notifyItemChanged(updatePosition)
                                                }
                                       }

                                    }
                                }
                            }
                        })

                        (usersRecyclerView.adapter as UsersListAdapter).setAppContext(activity!!.applicationContext)
                        this@UserListFragment.activity?.let { it1 ->
                            (usersRecyclerView.adapter as UsersListAdapter).setActivContext(
                                it1
                            )
                        }

                        usersRecyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
                    }

                }

            }
        })

    }

    fun updateUser(email: String, password: String, nombre: String, admin: Boolean, active: Boolean,
                   telefono: String, apellido: String, userId: Int, mainView: View, position: Int, then: ((User) -> Unit)) {

        var url: String = requireActivity().getString(R.string.backEndHost) +"usuarios/update/" + userId + "/"
        val client = OkHttpClient()

        sharedPrefs = activity?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE) ?: return
        Token = sharedPrefs.getString("token","token")


        updatePosition = position
        val myBuilder: FormBody.Builder = FormBody.Builder()

        myBuilder.add("email",email)
            .add("telefono", "+$telefono")
            .add("apellido",apellido)
            .add("is_admin",admin.toString())
            .add("is_active",active.toString())
            .add("nombre",nombre)

        if(password != requireActivity().getString(R.string.noChangePass)){
            myBuilder.add("password",password)
        }

        var body: FormBody = myBuilder.build()

        var request = Request.Builder().url(url).header(
            "Authorization",
            Token
        ).patch(body).build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    //println(response.body!!.string())

                    var new = JSONObject(response.body!!.string())

                    if (response.isSuccessful) {

                        var tempUser = JSONObject(new.toString())

                        val gson = GsonBuilder().create()
                        val res: User = gson.fromJson(tempUser.toString(),User::class.java)
                        then(res)

                        //tempUser.put()
                    }
                }
            }
        })

    }

}




class UserFeed(val users: MutableList<User>)


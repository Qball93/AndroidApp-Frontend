package com.example.androideventapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.androideventapp.helpers.*
import com.example.androideventapp.models.User
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.user_card_row.view.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlin.properties.Delegates


class UsersListAdapter(private val userList: MutableList<User>, private val context: UserListFragment, var itemClickListener: ItemClickListener): RecyclerView.Adapter<CustomViewHolder>(){

    private lateinit var data: MutableList<User>
    private lateinit var appContext: Context
    private lateinit var activContext: Context
    var updatePosition: Int = 0


    

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.user_card_row,parent, false )
        setList(userList)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {


        val user:User = userList[position]

        holder.view.nameView?.text = user.nombre + " " +user.apellido
        holder.view.emailView.text = user.email
        holder.view.phoneView.text = user.telefono

        if(user.last_login === null){
            holder.view.loginView.text = "N/A"
        }else{
            holder.view.loginView.text = user.last_login.toString()
        }

        holder.view.eventView.text = user.events.toString()

        holder.view.ExpandableLayout.visibility = if(user.expanded) View.VISIBLE else View.GONE
        //Todo Find out why the layouts doesnt expand/contract properly



        holder.view.myCardView.setOnClickListener{
            user.expanded = !user.expanded

            println(user.expanded)


            notifyItemChanged(position);
        }

        holder.view.editButton.setOnClickListener{
            itemClickListener.itemClick(user, position)
        }
/*
        holder.view.editButton.setOnClickListener{
            //Todo change to actual stored email instead of literal once done testing
            var currentUserEmail :String = "testemail@email.com"

            var popupView: View = LayoutInflater.from(appContext).inflate(R.layout.create_user_window,null)

            var width: Int = 900;
            var height: Int = LinearLayout.LayoutParams.WRAP_CONTENT;
            var focusable: Boolean = true;
            var popup = PopupWindow(popupView,width,height,focusable)

            popup.showAtLocation(holder.view, Gravity.CENTER,0,100)

            var inputButton: Button = popupView.findViewById<Button>(R.id.buttonInput)
            inputButton.text = "Editar"

            var editNombre : EditText = popupView.findViewById<EditText>(R.id.editTextNombre)
            editNombre.setText(user.nombre)

            var editApellido : EditText = popupView.findViewById<EditText>(R.id.editTextApellido)
            editApellido.setText(user.apellido)

            var editEmail: EditText = popupView.findViewById<EditText>(R.id.editTextEmail)
            editEmail.setText(user.email)

            var editPhone: EditText = popupView.findViewById<EditText>(R.id.editTextTelephone)
            editPhone.setText(user.telefono)

            var editPassword: EditText = popupView.findViewById<EditText>(R.id.editTextPassword1)
            editPassword.setText("samepassiuj")

            var editPassword2: EditText = popupView.findViewById<EditText>(R.id.editTextPassword2)
            editPassword2.setText("samepassiuj")

            var adminCheck : CheckBox = popupView.findViewById<CheckBox>(R.id.adminBox)
            var activeCheck : CheckBox = popupView.findViewById<CheckBox>(R.id.is_active)

            if(user.email == currentUserEmail){
                adminCheck.isChecked = true
                adminCheck.isClickable = false
                activeCheck.isChecked = true
                activeCheck.isClickable = false
            }
            
            inputButton.setOnClickListener{
                if (validateEmail(editEmail) and
                    validatePhone(editPhone) and
                    validateNoEmptySpaces(editNombre) and
                    validateNoEmptySpaces(editApellido) and
                    validateIsEmpty(editPassword) and
                    validateIsEmpty(editPassword2)
                ) {
                    if (editPassword.text.toString() != editPassword2.text.toString()) {
                        customDialogue(activContext, "Contrasenas no son iguales", "alert")
                    }else{
                        updateUser(editEmail.text.toString(),editPassword.text.toString(),editNombre.text.toString(),adminCheck.isChecked,
                            activeCheck.isChecked, editPhone.text.toString(), editApellido.text.toString(),user.id, popupView, position){
                             res -> replaceItem(res,this.updatePosition)
                            popup.dismiss()
                            //activContext.run{
                            //customDialogue(activContext, "Usuario actualizado", "success")
                        }
                        //notifyItemChanged(this.updatePosition)

                    }
                }
            }
        }
*/

    }

    interface ItemClickListener {
        fun itemClick(myUser :User, position: Int)
    }


    fun getListItems(): MutableList<User> {
        return data
    }

    fun setList(data: MutableList<User>) {
        this.data = data
    }

    fun setAppContext(ParentApp: Context) {
        this.appContext = ParentApp
    }

    fun setActivContext(ParentActivity: Context){
        this.activContext = ParentActivity
    }

  /*  fun updateUser(email: String, password: String, nombre: String, admin: Boolean, active: Boolean,
                   telefono: String, apellido: String, userId: Int, mainView: View, positon: Int, then: ((User) -> Unit)) {
        var url: String = activContext.getString(R.string.backEndHost) +"usuarios/update/" + userId + "/"
        val client = OkHttpClient()

        this.updatePosition = positon
        val myBuilder: FormBody.Builder = FormBody.Builder()

        myBuilder.add("email",email)
            .add("telefono", "+$telefono")
            .add("apellido",apellido)
            .add("is_admin",admin.toString())
            .add("is_active",active.toString())
            .add("nombre",nombre)

        if(password != activContext.getString(R.string.noChangePass)){
            myBuilder.add("password",password)
        }

        var body: FormBody = myBuilder.build()

        var request = Request.Builder().url(url).header(
            "Authorization",
            "Token b29d64a6178158d6a7fa0b2d5f49e109d28358e2"
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

    }*/


    fun addItem(item: User) {
        userList.add(item)
    }

    fun replaceItem(item: User, position: Int) {

        userList[position] = item
        println(userList)
    }

}


class CustomViewHolder(val view : View): RecyclerView.ViewHolder(view) {
   init {

   }
}


package com.example.androideventapp.fragments
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import com.example.androideventapp.R
import com.example.androideventapp.helpers.*
import com.example.androideventapp.models.User
import kotlinx.android.synthetic.main.user_change_pass_window.*
import kotlinx.android.synthetic.main.user_configuration_fragment.*


class UserSettingsFragment(): Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.user_configuration_fragment, container, false)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        context?.let {
            fetchUserforUpdate(it){
                setMenu(it)
            }
        }


        btPass.setOnClickListener() {
            var popupView: View = layoutInflater.inflate(R.layout.user_change_pass_window, null)

            var width: Int = 900;
            var height: Int = LinearLayout.LayoutParams.WRAP_CONTENT;
            var focusable: Boolean = true;
            var popup = PopupWindow(popupView,width,height,focusable)

            popup.showAtLocation(view, Gravity.CENTER,0,100)

            var password1 : EditText = popupView.findViewById<EditText>(R.id.userConfigPassChange)
            var password2: EditText = popupView.findViewById<EditText>(R.id.userConfigPassChangeRepeat)

            var inputButton: Button = popupView.findViewById<Button>(R.id.changeUserPass)


            inputButton.setOnClickListener {
                if(password1.text.toString() != password2.text.toString()){
                    this.context?.let { it1 -> customDialogue(it1,"Contraseñas no son iguales","warning") }
                }else{

                    context?.let {
                            it1 -> updateUsersPassword(it1,password1.text.toString())
                            customDialogue(it1,"Contraseña Actualizada", "success")
                    }
                }
            }

        }

        btUpdate.setOnClickListener() {

            var email: EditText = activity?.findViewById<EditText>(R.id.userConfigemailText)!!
            var phone: EditText = activity?.findViewById<EditText>(R.id.userConfigPhoneTexxt)!!
            var nombre: EditText = activity?.findViewById<EditText>(R.id.userConfigNameText)!!
            var apellido: EditText = activity?.findViewById<EditText>(R.id.userConfigApellidoText)!!

            if (validateEmail(email) and
                validatePhone(phone) and
                validateNoEmptySpaces(nombre) and
                validateNoEmptySpaces(apellido)
            ){
                this.activity?.let { it1 -> updateUserGeneralInfo(it1,email.text.toString(),"+"+phone.text.toString(),
                nombre.text.toString(),apellido.text.toString()){
                    setMenu(it)
                    requireActivity().runOnUiThread { customDialogue(requireActivity(),"Datos Actualizados","success") }


                } }
            }
        }
    }




    fun setMenu(updateUser: User) {

        activity?.runOnUiThread{

            activity?.findViewById<EditText>(R.id.userConfigNameText)!!.setText(updateUser.nombre)
            activity?.findViewById<EditText>(R.id.userConfigApellidoText)!!.setText(updateUser.apellido)
            activity?.findViewById<EditText>(R.id.userConfigPhoneTexxt)!!.setText(updateUser.telefono.drop(1))
            activity?.findViewById<EditText>(R.id.userConfigemailText)!!.setText(updateUser.email)
        }


    }


}



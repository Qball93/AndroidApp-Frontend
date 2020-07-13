package com.example.androideventapp.helpers

import android.text.TextUtils
import android.widget.EditText
import java.util.regex.Pattern



    fun validateEmail(inputEmail : EditText): Boolean{

        return if(!(validateIsEmpty(
                inputEmail
            ))){
            false;
        }
        else if(!(android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail.text.toString()).matches())){
            inputEmail.error = "Por favor ingrese un correo electronico valido"
            false;
        } else {
            true;
        }
    }

    fun validateIsEmpty(inputName: EditText): Boolean {

        return if(TextUtils.isEmpty(inputName.text.toString())){
            inputName.error = "Este campo no puede estar vacio"
            false;
        } else {
            true;
        }
    }

    fun validatePhone(inputPhone: EditText) : Boolean {

        return if(!(validateIsEmpty(
                inputPhone
            ))){
            false
        }else if(!(Pattern.matches("^504[1-9][0-9]{7}\$", inputPhone.text.toString()))){
            inputPhone.error = "Por favor ingrese un numero de telefono valido que empieze con 504."
            false
        } else{
            true
        }
    }

    fun validateNoEmptySpaces(inputText: EditText) : Boolean {

        return if(!(validateIsEmpty(inputText))){
            false
        } else if(!(Pattern.matches("^[A-Za-z]+\$", inputText.text.toString()))){
            inputText.error = "No se permiten espacios vacios en este campo."
            false
        }else{
            true
        }
    }

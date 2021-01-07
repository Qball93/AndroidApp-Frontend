package com.example.androideventapp.helpers

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import com.example.androideventapp.R
import com.example.androideventapp.fragments.UserSettingsFragment
import kotlinx.android.synthetic.main.alert_dialog_error.view.*


fun customDialogue(context: Context, message: String, type: String) {

    val mDialogView: View = when(type){
        "error" -> LayoutInflater.from(context).inflate(R.layout.alert_dialog_error, null)
        "success" -> LayoutInflater.from(context).inflate(R.layout.alert_dialog_success, null)
        "warning" -> LayoutInflater.from(context).inflate(R.layout.alert_dialog_warning, null)
        else -> LayoutInflater.from(context).inflate(R.layout.alert_dialog_warning, null)
    }

    val alertDialog: AlertDialog = AlertDialog.Builder(context).setView(mDialogView).create()
    mDialogView.text_dialog.text = message
    alertDialog.show()

    mDialogView.btn_dialog.setOnClickListener{
        alertDialog.dismiss()
    }

}



fun clearChildren(parentGroup: ViewGroup) {

    for(count in 0 until parentGroup.childCount){
        var child :View = parentGroup.getChildAt(count)

        when(child){
            is ViewGroup -> clearChildren(child)
            is EditText -> child.setText("")
            is CheckBox -> child.isChecked = false
        }
    }
}
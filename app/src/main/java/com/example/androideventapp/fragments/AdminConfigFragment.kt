package com.example.androideventapp.fragments

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.androideventapp.R
import com.example.androideventapp.helpers.changeTypeStatus
import com.example.androideventapp.helpers.createNewType
import com.example.androideventapp.helpers.customDialogue
import com.example.androideventapp.helpers.fetchTypes
import com.example.androideventapp.models.EventType
import kotlinx.android.synthetic.main.admin_config_fragment.*

class AdminConfigFragment: Fragment() {



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        return inflater.inflate(R.layout.admin_config_fragment, container, false)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val res: Resources = resources
        val colorName: Array<String> = res.getStringArray(R.array.colorNames)
        val colorValue: Array<String> = res.getStringArray(R.array.colorValues)

        fetchTypes(requireActivity(),"true"){ activeTypes ->
            fetchTypes(requireActivity(),"false"){ inActiveTypes ->

                activity?.runOnUiThread{
                    val activeSpinner: Spinner? = activity?.findViewById<Spinner>(R.id.ActiveTypeSpinner)
                    val inactiveSpinner: Spinner? = activity?.findViewById<Spinner>(R.id.InactiveTypeSpinner)
                    val colorSpinner: Spinner? = activity?.findViewById<Spinner>(R.id.colorSpinner)

                    val spinnerAdapter3 : ArrayAdapter<String> = ArrayAdapter<String>(
                        this.context,android.R.layout.simple_spinner_item,
                        colorName
                    )

                    val spinnerAdapter : ArrayAdapter<EventType> = ArrayAdapter<EventType>(
                        this.context,
                        android.R.layout.simple_spinner_item,
                        activeTypes
                    )
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    val spinnerAdapter2 : ArrayAdapter<EventType> = ArrayAdapter<EventType>(
                        this.context,
                        android.R.layout.simple_spinner_item,
                        inActiveTypes
                    )
                    spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    spinnerAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    if (colorSpinner != null) {
                        colorSpinner.adapter = spinnerAdapter3
                    }
                    if (activeSpinner != null) {
                        activeSpinner.adapter = spinnerAdapter
                    }
                    if (inactiveSpinner != null) {
                        inactiveSpinner.adapter = spinnerAdapter2
                    }

                    btActivate.setOnClickListener {

                        if (inactiveSpinner != null) {
                            changeTypeStatus(requireActivity(),inActiveTypes[inactiveSpinner.selectedItemPosition].id,"true"){
                                customDialogue(requireActivity(),"Status Actualizado",it)
                            }
                        }
                    }

                    btDeactivate.setOnClickListener {
                        if (activeSpinner != null) {
                            changeTypeStatus(requireActivity(),inActiveTypes[activeSpinner.selectedItemPosition].id,"false"){
                                customDialogue(requireActivity(),"Status Actualizado",it)
                            }
                        }
                    }

                    btCreateNew.setOnClickListener {
                        var nameText = requireActivity().findViewById<EditText>(R.id.editTextTextPersonName2)
                        if(nameText.text.toString() != ""){
                            if (colorSpinner != null) {
                                createNewType(requireActivity(),colorValue[colorSpinner.selectedItemPosition],nameText.text.toString()){
                                    customDialogue(requireActivity(),"Tipo Creado","success")
                                }
                            }
                        }else{
                            customDialogue(requireActivity(),"Porfavor ingrese un nombre para el tipo de Evento","warning")
                        }


                    }



                }


            }

        }

    }

}
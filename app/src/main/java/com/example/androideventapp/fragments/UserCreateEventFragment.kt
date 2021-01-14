package com.example.androideventapp.fragments

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.androideventapp.R
import com.example.androideventapp.helpers.createEvent
import com.example.androideventapp.helpers.customDialogue
import com.example.androideventapp.helpers.fetchTypes
import com.example.androideventapp.models.EventType
import com.example.androideventapp.models.SimpleUser
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.event_reporting_fragment.*

class UserCreateEventFragment: Fragment(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private lateinit var locationRequest: LocationRequest
    val RequestPermissionCode = 1
    var mLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var latitude: String
    private lateinit var longitude: String


    override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)





        map_View.onCreate(savedInstanceState)
        map_View.onResume()

        map_View.getMapAsync(this)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.event_reporting_fragment, container, false)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchTypes(requireActivity(),"true"){ tipos->

            activity?.runOnUiThread {
                val mySpinner: Spinner = activity?.findViewById<Spinner>(R.id.spinner)!!



                val spinnerAdapter : ArrayAdapter<EventType> = ArrayAdapter<EventType>(
                    this.context,
                    android.R.layout.simple_spinner_item,
                    tipos
                )
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


                mySpinner.adapter = spinnerAdapter

                btReport.setOnClickListener {


                    createEvent(requireActivity(),latitude,longitude,tipos[mySpinner.selectedItemPosition].id){


                        requireActivity().runOnUiThread {
                            customDialogue(requireActivity(),"Evento Creado con Exito", "success") }
                    }

                }

            }
        }

    }

    fun getLastLocation(currentMap : GoogleMap) {

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission()
        } else {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    mLocation = location
                    if (location != null) {
                        println("inside this dumb shit")
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()

                        currentMap.clear()
                        println("this is longitude"+longitude+ "and this is latitude"  + latitude)

                        val boundingBox = LatLngBounds(
                            LatLng((latitude.toDouble()-0.4), longitude.toDouble()-0.4),
                            LatLng(latitude.toDouble()+0.4,longitude.toDouble()+0.4)
                        )

                        currentMap.apply {
                            val location = LatLng(latitude.toDouble(),longitude.toDouble())

                            mMap.addMarker(
                                MarkerOptions().position(location)
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            34.toFloat()
                                        )
                                    ).title("Ubicacion Actual")
                            )
                        }

                        currentMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundingBox, 0))


                    }
                }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            RequestPermissionCode
        )
    }

    override fun onMapReady(map: GoogleMap) {

        map.let{
            mMap = it
        }

        val lm = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager


        if (lm != null) {
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            ) {
                // Build the alert dialog
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle("Servicios de ubicacion no estan activos")
                builder.setMessage("Porfavor habilitar los servicios de ubicacion y GPS")
                builder.setPositiveButton("OK",
                    DialogInterface.OnClickListener { _, _ -> // Show location settings when the user acknowledges the alert dialog
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    })
                val alertDialog: Dialog = builder.create()
                alertDialog.setCanceledOnTouchOutside(false)
                alertDialog.show()
            }else{

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
                getLastLocation(mMap)
            }

        }
    }

}
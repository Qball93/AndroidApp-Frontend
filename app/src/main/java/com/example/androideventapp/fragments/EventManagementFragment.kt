package com.example.androideventapp.fragments;

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.androideventapp.CustomInfoWindowForGoogleMap
import com.example.androideventapp.R
import com.example.androideventapp.helpers.*
import com.example.androideventapp.models.Event
import com.example.androideventapp.models.EventType
import com.example.androideventapp.models.SimpleUser
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.event_filter_window.*
import kotlinx.android.synthetic.main.event_management_fragment.*
import kotlinx.android.synthetic.main.user_list_fragment.*
import okhttp3.*
import java.io.IOException
import java.util.*


class EventManagementFragment: Fragment(), OnMapReadyCallback {

        private lateinit var mMap: GoogleMap
        private lateinit var sharedPrefs: SharedPreferences
        private lateinit var Token: String


        override fun onActivityCreated(savedInstanceState: Bundle?){
                super.onActivityCreated(savedInstanceState)

                sharedPrefs = activity?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE) ?: return
                Token = "Token " + sharedPrefs.getString("token","")


                map_view.onCreate(savedInstanceState)
                map_view.onResume()

                map_view.getMapAsync(this)





        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)

                fetchTypes(requireActivity()) { Tipos ->
                        fetchSimpleUserList { Usuarios ->
                                Usuarios.add(0, SimpleUser(null, "N/A", ""))


                                filterButton.setOnClickListener {

                                        var counter: Int = 1
                                        var paramCounter: Int = 0

                                        var popupView: View = layoutInflater.inflate(
                                                R.layout.event_filter_window,
                                                null
                                        )

                                        var root: ViewGroup =
                                                popupView.findViewById<View>(R.id.EventCreator) as ViewGroup

                                        var layoutCounter: Int = 0

                                        for (Tipo in Tipos) {


                                                if ((counter - 1).rem(3) == 0) {


                                                        val layoutBuild = LinearLayout(this.context)

                                                        layoutCounter = counter

                                                        layoutBuild.orientation =
                                                                LinearLayout.HORIZONTAL

                                                        layoutBuild.layoutParams =
                                                                LinearLayout.LayoutParams(
                                                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                        FrameLayout.LayoutParams.WRAP_CONTENT
                                                                )

                                                        layoutBuild.tag = "layout$layoutCounter"

                                                        val checkboxType = CheckBox(this.context)

                                                        checkboxType.text = Tipo.nombre
                                                        checkboxType.tag = "tipoID" + Tipo.id

                                                        layoutBuild.addView(checkboxType)

                                                        root.addView(layoutBuild)

                                                } else {
                                                        val layoutBuild =
                                                                root.findViewWithTag<LinearLayout>(
                                                                        "layout$layoutCounter"
                                                                )

                                                        println(layoutBuild)
                                                        val checkboxType = CheckBox(this.context)

                                                        checkboxType.text = Tipo.nombre
                                                        checkboxType.tag = "tipoID" + Tipo.id

                                                        layoutBuild.addView(checkboxType)

                                                        //root.addView(layoutBuild)
                                                }

                                                counter++
                                        }


                                        var width: Int = 900;
                                        var height: Int = LinearLayout.LayoutParams.WRAP_CONTENT;
                                        var focusable: Boolean = true;
                                        var popup = PopupWindow(popupView, width, height, focusable)

                                        popup.showAtLocation(view, Gravity.CENTER, 0, 100)

                                        val mySpinner: Spinner = popupView.findViewById<Spinner>(R.id.userSpinner)



                                        val spinnerAdapter : ArrayAdapter<SimpleUser> = ArrayAdapter<SimpleUser>(
                                                this.context,
                                                android.R.layout.simple_spinner_item,
                                                Usuarios
                                        )
                                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                                        mySpinner.adapter = spinnerAdapter

                                        var SimpleFormat =
                                                SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")

                                        var startDate: Date? = null
                                        var sDate:  String = ""
                                        var eDate:  String = ""
                                        var endDate: Date? = null


                                        var expanded: Boolean = false

                                        var endButton: Button =
                                                popupView.findViewById<Button>(R.id.endDate)
                                        var startButton: Button =
                                                popupView.findViewById<Button>(R.id.startDate)
                                        var startText: TextView =
                                                popupView.findViewById<TextView>(R.id.startDateText)
                                        var endText: TextView =
                                                popupView.findViewById<TextView>(R.id.endDateText)
                                        var searchButton: Button =
                                                popupView.findViewById<Button>(R.id.search)

                                        startButton.setOnClickListener {
                                                val c = Calendar.getInstance()
                                                val year = c.get(Calendar.YEAR)
                                                val month = c.get(Calendar.MONTH)
                                                val day = c.get(Calendar.DAY_OF_MONTH)

                                                val dpd = DatePickerDialog(
                                                        activity,
                                                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                                                                startDate = SimpleFormat.parse(
                                                                        view.getDate().toString()
                                                                )
                                                                startText.text =
                                                                        startDate.toString()

                                                        },
                                                        year,
                                                        month,
                                                        day
                                                )
                                                dpd.show()

                                                if (endDate != null) {
                                                        dpd.datePicker.maxDate = endDate!!.time
                                                }
                                        }


                                        endButton.setOnClickListener {
                                                val c = Calendar.getInstance()
                                                val year = c.get(Calendar.YEAR)
                                                val month = c.get(Calendar.MONTH)
                                                val day = c.get(Calendar.DAY_OF_MONTH)

                                                val dpd = DatePickerDialog(
                                                        activity,
                                                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->


                                                                endDate = view.getDate()
                                                                endText.text = endDate.toString()


                                                        },
                                                        year,
                                                        month,
                                                        day
                                                )


                                                if (startDate != null) {
                                                        dpd.datePicker.minDate = startDate!!.time
                                                }
                                                dpd.show()


                                        }





                                        searchButton.setOnClickListener {
                                                var tiposEventos: String = ""
                                                var myFormat: DateFormat =
                                                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                                                var userId : String? = Usuarios[mySpinner.selectedItemPosition].id.toString()



                                                for (Tipo in Tipos) {

                                                        var currentBox: CheckBox =
                                                                popupView.findViewWithTag("tipoID" + Tipo.id)

                                                        if (currentBox.isChecked) {
                                                                tiposEventos =
                                                                        tiposEventos + Tipo.id + ","
                                                        }
                                                }

                                                if (tiposEventos != "") {

                                                        println(tiposEventos.last())
                                                        if (tiposEventos.last() == ',') {
                                                                println("inside loop")
                                                                tiposEventos =
                                                                        "TipoEvento=" + tiposEventos.dropLast(
                                                                                1
                                                                        ) + "&"
                                                        }


                                                }

                                                if (startDate != null) {
                                                        sDate = "startDate=" + myFormat.format(
                                                                startDate
                                                        ) + "&"
                                                }
                                                if (endDate != null) {
                                                        eDate =
                                                                "endDate=" + myFormat.format(endDate) + "&"
                                                }
                                                if(userId != null) {
                                                        userId = "User="+userId+"&"
                                                }
                                                fetchEventJson(
                                                        sDate,
                                                        eDate,
                                                        tiposEventos,
                                                        userId
                                                ) { Eventos ->


                                                        createMarkers(Eventos, mMap)

                                                }


                                        }

                                }
                        }
                }





        }

        override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?

        ): View {
                // Inflate the layout for this fragment

                return inflater.inflate(R.layout.event_management_fragment, container, false)

                /*val mapFragment = fragmentManager?.findFragmentById(R.id.map_view) as SupportMapFragment
                mapFragment.getMapAsync(this)*/

        }

        override fun onMapReady(map: GoogleMap) {

                map.let{
                        mMap = it
                }


                fetchEventJson(null, null, null, null){ Eventos  ->

                        createMarkers(Eventos, mMap)
                        activity?.runOnUiThread {
                                mMap.setInfoWindowAdapter(
                                        CustomInfoWindowForGoogleMap(requireActivity())

                                )
                        }
                }
        }
/*
        fun fetchTypes(then: ((MutableList<EventType>) -> Unit)){
                var url: String = getString(R.string.backEndHost) + "events/tiposEvent"

                sharedPrefs = activity?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE) ?: return
                Token = "Token " + sharedPrefs.getString("token","token")


                val client = OkHttpClient()
                val myString: String
                //Token for testing purposes
                var request = Request.Builder().url(url).header(
                        "Authorization",
                        Token
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

                                        val Tipos = gson.fromJson(
                                                body,
                                                Array<EventType>::class.java
                                        ).toMutableList()

                                        then(Tipos)

                                }
                        }
                })
        }
*/
        fun fetchEventJson(
                startDate: String?,
                endDate: String?,
                TipoEvento: String?,
                userId: String?,
                then: ((MutableList<Event>) -> Unit)
        ){

                var url: String = getString(R.string.backEndHost) + "events/filteredEvents/?"+startDate+endDate+TipoEvento+userId
                val client = OkHttpClient()
                sharedPrefs = activity?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE) ?: return
                Token = sharedPrefs.getString("token","token")


                if(url.last() == '&'){
                        url = url.dropLast(1)
                }


                var request = Request.Builder().url(url).header(
                        "Authorization",
                        Token
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

                                        val Eventos = gson.fromJson(body, Array<Event>::class.java)
                                                .toMutableList()

                                        then(Eventos)

                                }
                        }
                })
        }

        fun createMarkers(mapEvents: MutableList<Event>, currentMap: GoogleMap){
                activity?.runOnUiThread{
                        currentMap.clear()

                        val Roatan = LatLngBounds(
                                LatLng(15.9643, -87.073737),
                                LatLng(16.660217, -85.767737)
                        )
                        currentMap.apply{

                                var index: Int = 0

                                for(event in mapEvents){

                                        val location = LatLng(event.coordsx, event.coordsy)

                                        var currentMark : Marker = currentMap.addMarker(
                                                MarkerOptions().position(location)
                                                        .icon(
                                                                BitmapDescriptorFactory.defaultMarker(
                                                                        event.TipoEvento.color.toFloat()
                                                                )
                                                        )
                                                        .title("test")
                                                        .snippet("testtt")
                                        )
                                        currentMark.tag = event
                                        index++
                                }
                        }
                        currentMap.moveCamera(CameraUpdateFactory.newLatLngBounds(Roatan, 0))
                }
        }

        fun DatePicker.getDate(): Date {
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                return calendar.time
        }

        fun fetchSimpleUserList(then: ((MutableList<SimpleUser>) -> Unit)){
                var url: String = getString(R.string.backEndHost) + "usuarios/simpleUser/"
                val client = OkHttpClient()

                sharedPrefs = activity?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE) ?: return
                Token = sharedPrefs.getString("token","token")


                var request = Request.Builder().url(url).header(
                        "Authorization",
                        Token
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

                                        val Usuarios = gson.fromJson(
                                                body,
                                                Array<SimpleUser>::class.java
                                        )
                                                .toMutableList()

                                        then(Usuarios)

                                }
                        }
                })
        }
}



/*
class EventFeed(val eventos: MutableList<Event>)

class TypeFeed(val tipos: MutableList<EventType>)*/
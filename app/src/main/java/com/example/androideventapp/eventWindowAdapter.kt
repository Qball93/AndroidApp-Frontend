package com.example.androideventapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.androideventapp.models.Event
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import org.w3c.dom.Text

class CustomInfoWindowForGoogleMap(context: Context) : GoogleMap.InfoWindowAdapter {



    var mContext = context
    @SuppressLint("InflateParams")
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.event_update_window, null)


    private fun rendowWindowText(marker: Marker, view: View){


        var currentEvent: Event = marker.tag as Event
        val viewName = view.findViewById<TextView>(R.id.userNameView)
        val viewDate = view.findViewById<TextView>(R.id.dateView)
        val viewType = view.findViewById<TextView>(R.id.typeView)
        val viewLat = view.findViewById<TextView>(R.id.latView)
        val viewLong = view.findViewById<TextView>(R.id.longView)

        viewName.text = currentEvent.Usuario.nombre + " " + currentEvent.Usuario.apellido
        viewDate.text = currentEvent.fechaEvento.toString()
        viewType.text = currentEvent.TipoEvento.nombre
        viewLat.text = currentEvent.coordsy.toString()
        viewLong.text = currentEvent.coordsx.toString()

    }

    override fun getInfoContents(marker: Marker): View {


        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {


        rendowWindowText(marker, mWindow)
        return mWindow
    }
}
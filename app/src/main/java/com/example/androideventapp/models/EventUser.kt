package com.example.androideventapp.models

import android.provider.ContactsContract

data class EventUser(
    var nombre: String,
    var apellido: String,
    var email: String,
    var id: Int
)
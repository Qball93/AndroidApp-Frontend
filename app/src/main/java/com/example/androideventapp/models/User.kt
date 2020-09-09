package com.example.androideventapp.models

import androidx.annotation.StringRes
import java.util.*

data class User (
    var nombre: String,
    var apellido: String,
    var is_admin: Boolean,
    var email: String,
    var last_login: Date,
    var is_active: Boolean,
    var events: Int,
    var telefono: String,
    var id: Int,
    var expanded: Boolean = false

)
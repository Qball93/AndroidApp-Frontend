package com.example.androideventapp.models

import java.util.*

data class Event (
    var coordsx : Double,
    var coordsy : Double,
    var fechaEvento: Date,
    var Usuario: EventUser,
    var TipoEvento: EventType,
    var id: Int,
    var Activo: Boolean
)
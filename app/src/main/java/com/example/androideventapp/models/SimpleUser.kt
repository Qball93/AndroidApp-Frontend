package com.example.androideventapp.models

data class SimpleUser(
    var id: Int?,
    var nombre: String,
    var apellido: String
) {
    override fun toString(): String = nombre + " " + apellido
}


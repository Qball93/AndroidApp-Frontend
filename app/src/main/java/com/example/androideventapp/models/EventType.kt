package com.example.androideventapp.models

data class EventType(
    var id: Int,
    var nombre: String,
    var color: String
){
    override fun toString(): String {
        return nombre
    }
}
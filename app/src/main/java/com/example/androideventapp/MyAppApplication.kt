package com.example.androideventapp

import android.app.Application


internal class MyAppApplication : Application() {
    var globalVarValue: String? = null
    var mExpandedPosition: Int = -1
}
package com.example.androideventapp

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.androideventapp.fragments.UserCreateEventFragment
import com.example.androideventapp.fragments.UserSettingsFragment
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.user_main.*


class UserMenu : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private lateinit var btn: Button
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var locationRequest: LocationRequest
    private val userFragment = UserSettingsFragment()
    private val reportFragment = UserCreateEventFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_main)


        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.user_drawer_layout)

        toggle  = ActionBarDrawerToggle(
            this,
            drawer,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()


        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, userFragment)
        fragmentTransaction.commit()



        nav_view_user.setNavigationItemSelectedListener {

            var size: Int = nav_view_user.menu.size()

            for (i in 0 until size) {
                nav_view_user.menu.getItem(i).isChecked = false
            }

            when(it.itemId){
                R.id.nav_config -> {
                    it.isChecked = true
                    setFragment(userFragment)
                }
                R.id.nav_event -> {
                    //navigationView.itemBackgroundResource = R.color.colorPrimaryDark
                    it.isChecked = true
                    setFragment(reportFragment)
                }

            }
            false
        }


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


}


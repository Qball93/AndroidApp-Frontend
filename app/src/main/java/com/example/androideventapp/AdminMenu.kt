package com.example.androideventapp


import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.androideventapp.fragments.AdminConfigFragment
import com.example.androideventapp.fragments.EventManagementFragment
import com.example.androideventapp.fragments.NewUserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.admin_main.*
import kotlinx.android.synthetic.main.user_main.*

class AdminMenu : AppCompatActivity(){


    private val fragmentManager = supportFragmentManager
    private val userFragment = UserListFragment()
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private val eventFragment = EventManagementFragment()
    private val adminConfigFragment = AdminConfigFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_main)



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

        /* Display First Fragment initially */
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, userFragment)
        fragmentTransaction.commit()

        nav_view_admin.setNavigationItemSelectedListener {item ->

            var size: Int = nav_view_admin.menu.size()

            for (i in 0 until size) {
                nav_view_admin.menu.getItem(i).isChecked = false
            }


            when (item.itemId){
                R.id.nav_user -> {
                    item.isChecked = true
                    setFragment(userFragment)
                }
                R.id.nav_event -> {
                    //navigationView.itemBackgroundResource = R.color.colorPrimaryDark
                    item.isChecked = true
                    setFragment(eventFragment)
                }
                R.id.nav_configs -> {
                    //navigationView.itemBackgroundResource = R.color.colorPrimaryDark
                    item.isChecked = true
                    setFragment(adminConfigFragment)
                }
            }
            false
        }


    }

    /*fun btnOne(v: View){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.myFragment, userFragment)
        fragmentTransaction.commit()
    }

    fun btnTwo(v: View){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.myFragment, eventFragment)
        fragmentTransaction.commit()
    }*/

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}
package com.example.androideventapp

import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androideventapp.models.User
import kotlinx.android.synthetic.main.user_card_row.view.*


class UsersListAdapter(private val userList: Array<User>): RecyclerView.Adapter<CustomViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.user_card_row,parent, false )
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {


        var user:User = userList.get(position)

        holder.view.nameView?.text = user.nombre + " " +user.apellido
        holder.view.emailView.text = user.email
        holder.view.phoneView.text = user.telefono
        if(user.last_login == null){
            holder.view.loginView.text = "N/A"
        }else{
            holder.view.loginView.text = user.last_login.toString()
        }
        holder.view.eventView.text = user.events.toString()

        holder.view.ExpandableLayout.visibility = if(user.expanded) View.VISIBLE else View.GONE
        //Todo Find out why the layouts doesnt expand/contract properly
        holder.view.setOnClickListener{
            user.expanded = !user.expanded

            notifyItemChanged(position);
        }
    }

}


class CustomViewHolder(val view : View): RecyclerView.ViewHolder(view) {
   /*init {
        view.setOnClickListener{
            println("test")
        }
    }*/
}


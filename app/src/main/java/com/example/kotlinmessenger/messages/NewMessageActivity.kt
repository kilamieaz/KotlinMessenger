package com.example.kotlinmessenger.messages

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

//        val adapter = GroupAdapter<ViewHolder>()
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        recyclerview_newmessage.adapter = adapter

        fetchUsers()
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }

    private fun fetchUsers() {
        // get all users
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        // addListenerForSingleValueEvent will constantly get called whatever you changing your data
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            // ondatachange = will get called every time you retrieve of the users inside of firebase database
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem  //
                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    //pass object from two different activities
                    //intent.putExtra(USER_KEY ,userItem.user.username)
                    intent.putExtra(USER_KEY ,userItem.user)

                    startActivity(intent)
                    finish()
                }
                recyclerview_newmessage.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}

class UserItem(val user: com.example.kotlinmessenger.models.User) : Item<ViewHolder>() {
    // edit data satuan
    override fun bind(viewHolder: ViewHolder, position: Int) {
        //will be called in our list for each user object
        viewHolder.itemView.username_textview_new_message.text = user.username
        // picasso change image
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_new_message)
    }
    // tampilan row satuan
    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}

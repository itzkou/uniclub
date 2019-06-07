package com.kou.uniclub.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.kou.uniclub.Adapter.RvChatAdapter
import com.kou.uniclub.Adapter.RvUsersAdapter.Companion.USER_KEY
import com.kou.uniclub.Model.Chat.Message
import com.kou.uniclub.Model.User.UserFire
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLog : AppCompatActivity() {
    private var msgs=ArrayList<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)


        imSend.setOnClickListener {
            sendMsg()
        }
        //rvChat.adapter=RvChatAdapter(arrayListOf(msg,msg2),context = this@ChatLog)

        listenMsgs()


    }

    private fun sendMsg(){

        val myID=FirebaseAuth.getInstance().uid
        if (myID!=null){
        val userID=intent.getParcelableExtra<UserFire>(USER_KEY)//The use I want to talk to
        val ref=FirebaseDatabase.getInstance().getReference("/messages").push() //creating fire Node
        val message=Message(ref.key!!,edMsg.text.toString(),myID,userID.uid,System.currentTimeMillis()/1000)
            ref.setValue(message)
                .addOnSuccessListener {
                    Log.d("sentMessage",ref.key)
                }
        }
    }
    private fun listenMsgs(){
        val ref=FirebaseDatabase.getInstance().getReference("/messages")
        ref.addChildEventListener(object:ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMsg=p0.getValue(Message::class.java)   //I HAVE USED THIS MODEL so I need to check the type of added child
                if (chatMsg!=null)
                    msgs.add(chatMsg)

                rvChat.adapter=RvChatAdapter(msgs,this@ChatLog)

            }

            override fun onChildRemoved(p0: DataSnapshot) {}

        })
    }
}

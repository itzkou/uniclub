package com.kou.uniclub.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kou.uniclub.Adapter.RvChatAdapter
import com.kou.uniclub.Adapter.RvUsersAdapter.Companion.USER_KEY
import com.kou.uniclub.Model.Chat.Message
import com.kou.uniclub.Model.User.UserFire
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLog : AppCompatActivity() {
    lateinit var msgs: ArrayList<Message>
    lateinit var adapter: RvChatAdapter
    private var toUser: UserFire? = null
    private var currentUser:UserFire?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        toUser = intent.getParcelableExtra(USER_KEY)
        msgs = ArrayList<Message>()
        adapter = RvChatAdapter(msgs, this@ChatLog)
        rvChat.adapter=adapter
        //fetchCurrentUser()
        imSend.setOnClickListener {
            sendMsg()
        }

        listenMsgs()
    }

    private fun sendMsg() {

        val text = edMsg.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<UserFire>(USER_KEY)
        val toId = user.uid

        if (fromId == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = Message(reference.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)

        reference.setValue(chatMessage)
            .addOnSuccessListener {
                edMsg.text.clear()
                rvChat.scrollToPosition(adapter.itemCount - 1)
            }

        toReference.setValue(chatMessage)

    }


    private fun listenMsgs() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(Message::class.java)

                if (chatMessage != null) {

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        adapter.add(chatMessage)
                    } else {
                        adapter.add(chatMessage)
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(UserFire::class.java)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
package com.kou.uniclub.Model.Chat

data class  Message(val id:String,val text:String,val fromId:String,val toId:String,val timeStamp:Long){
    constructor():this("","","","",-1)
}
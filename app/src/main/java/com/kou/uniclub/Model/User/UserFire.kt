package com.kou.uniclub.Model.User

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserFire(val uid:String,val username:String,val pic:String):Parcelable
{
    constructor():this("","","")
}
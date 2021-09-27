package com.example.chatbotui

import android.os.Parcelable
import java.io.Serializable

data class Remainder(
    var id: Int?,
    var medicineName:String,
    var description:String,
    var day:String,
    var month:String,
    var time:String
) : Serializable
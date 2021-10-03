package com.example.chatbotui.reminder

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.chatbotui.ReminderTable

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, "reminderlist.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(ReminderTable.CMD_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}
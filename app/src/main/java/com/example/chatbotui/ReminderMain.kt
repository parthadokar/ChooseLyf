package com.example.chatbotui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatbotui.*
import kotlinx.android.synthetic.main.reminder_activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReminderMain : AppCompatActivity() {

    var reminder_list = arrayListOf<Remainder>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reminder_activity_main)

        val dbhelper = SQLiteHelper(this)
        val reminderlist = dbhelper.writableDatabase

        val adapter = RemainerAdapter(reminder_list,this)
        rvreminder.layoutManager = LinearLayoutManager(this)
        rvreminder.adapter = adapter



        reminder_list = ReminderTable.getAllTask(reminderlist)
        adapter.notifyDataSetChanged()
        add_reminder.setOnClickListener{
            val intent = Intent(this,SetReminder::class.java)
            Log.i("reminder","setReminderActivityCalled")
            startActivity(intent)
        }


        adapter.reminderItemClickListener = object : ReminderItemClickListener {
            override fun onRemoveClick(reminder: Remainder, position: Int) {
                ReminderTable.deleteTask(reminderlist,reminder.id!!)
                reminder_list.removeAt(position)
                adapter.updateTasks(ReminderTable.getAllTask(reminderlist))
            }
        }

        val bundle = intent.extras
        var remlist = bundle?.getSerializable("remlist") as? ArrayList<Remainder>
        if(remlist!=null)
        {
            for (item in remlist){
                ReminderTable.insertreminder(reminderlist,item)
                Log.i("reminder","reminder inserted to the database")
                adapter.updateTasks(ReminderTable.getAllTask(reminderlist))
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onBackPressed() {

        val longbackpressed = System.currentTimeMillis()
        if(longbackpressed +2000 > System.currentTimeMillis())
        {
            super.onBackPressed()
            return;
        }else{
            Toast.makeText(baseContext,"Press back again to exit",Toast.LENGTH_SHORT).show()

        }

    }
}

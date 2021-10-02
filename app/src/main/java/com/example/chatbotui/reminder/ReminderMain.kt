package com.example.chatbotui.reminder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatbotui.MainActivity
import com.example.chatbotui.databinding.ReminderActivityMainBinding
import java.util.*

class ReminderMain : AppCompatActivity() {

    private val mAdapter: ReminderAdapter by lazy { ReminderAdapter() }

    private var _binding: ReminderActivityMainBinding? = null
    private val binding get() = _binding!!

    var reminderList = arrayListOf<Reminder>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ReminderActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbhelper = SQLiteHelper(this)
        val reminderlist = dbhelper.writableDatabase

        binding.reminderRecyclerview.adapter = mAdapter
        binding.reminderRecyclerview.layoutManager = LinearLayoutManager(this)

        reminderList = ReminderTable.getAllTask(reminderlist)
        mAdapter.notifyDataSetChanged()
        binding.addReminder.setOnClickListener {
            val intent = Intent(this, SetReminder::class.java)
            Log.i("reminder", "setReminderActivityCalled")
            startActivity(intent)
        }

        mAdapter.reminderItemClickListener = object : ReminderItemClickListener {
            override fun onRemoveClick(reminder: Reminder, position: Int) {
                ReminderTable.deleteTask(reminderlist, reminder.id!!)
                reminderList.removeAt(position)
                mAdapter.updateTasks(ReminderTable.getAllTask(reminderlist))
            }

        }

        val bundle = intent.extras
        val remlist = bundle?.getSerializable("remlist") as? ArrayList<*>
        if (remlist != null) {
            for (item in remlist) {
                ReminderTable.insertreminder(reminderlist, item as Reminder)
                Log.i("reminder", "reminder inserted to the database")
                mAdapter.updateTasks(ReminderTable.getAllTask(reminderlist))
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

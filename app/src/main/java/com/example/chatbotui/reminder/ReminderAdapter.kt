package com.example.chatbotui.reminder
/*
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbotui.databinding.ReminderItemBinding

class ReminderAdapter : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    var reminderList = arrayListOf<Reminder>()

    lateinit var reminderItemClickListener: ReminderItemClickListener

    class ReminderViewHolder(val binding: ReminderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun updateTasks(newReminders: ArrayList<Reminder>) {
        Log.i("reminder", "Update task in rv adapter")
        reminderList.clear()
        reminderList.addAll(newReminders)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        return ReminderViewHolder(
            ReminderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return reminderList.size
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {

        val reminderItem = reminderList[position]
        holder.itemView.apply {
            Log.i("reminder", "holder mapping")
            holder.binding.medName.text = reminderItem.medicineName
            holder.binding.medDescription.text = reminderItem.description
            holder.binding.date.text = reminderItem.day
            holder.binding.month.text = reminderItem.month
            holder.binding.time.text = reminderItem.time
        }
        holder.binding.Remove.setOnClickListener {
            reminderItemClickListener.onRemoveClick(reminderItem, position)
        }
    }

}
*/
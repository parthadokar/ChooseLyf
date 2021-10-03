package com.example.chatbotui.reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatbotui.databinding.ActivitySetReminderBinding
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class SetReminder : AppCompatActivity() {
    private var time1: Calendar? = null
    private var time2: Calendar? = null
    private var time3: Calendar? = null
    private var startday: Calendar? = null
    private var lastday: Calendar? = null

    private val am by lazy {
        getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private var _binding: ActivitySetReminderBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SimpleDateFormat", "WeekBasedYear")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySetReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pickTime1.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                binding.showTime1.text = SimpleDateFormat("hh:mm a").format(cal.time)
                time1 = cal
            }
            TimePickerDialog(
                this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                    Calendar
                        .MINUTE
                ), DateFormat.is24HourFormat(this)
            ).show()
        }

        binding.pickTime2.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                binding.showTime2.text = SimpleDateFormat("hh:mm a").format(cal.time)
                time2 = cal
            }
            TimePickerDialog(
                this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                    Calendar
                        .MINUTE
                ), DateFormat.is24HourFormat(this)
            ).show()
        }

        binding.pickTime3.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                binding.showTime3.text = SimpleDateFormat("hh:mm a").format(cal.time)
                time3 = cal
            }
            TimePickerDialog(
                this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                    Calendar
                        .MINUTE
                ), DateFormat.is24HourFormat(this)
            ).show()
        }

        binding.calenderStart.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            startday = cal
            Log.i("reminder", SimpleDateFormat("dd/MM/yyyy").format(startday!!.time))
            val datepicker = DatePickerDialog(
                this,
                { view, year, month, day ->
                    startday!!.set(year, month, day)
                    Log.i("reminder", SimpleDateFormat("dd/MM/yyyy").format(startday!!.time))
                    binding.startDate.text = ("$day/${month + 1}/$year")
                }, year, month, day
            )
            datepicker.show()

        }
        binding.calenderStop.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            lastday = cal
            Log.i("reminder", SimpleDateFormat("dd/MM/yyyy").format(lastday!!.time))
            val datepicker = DatePickerDialog(
                this,
                { view, year, month, day ->
                    lastday!!.set(year, month, day)
                    Log.i("reminder", SimpleDateFormat("dd/MM/yyyy").format(lastday!!.time))
                    binding.stopDate.text = ("$day/${month + 1}/$year")
                }, year, month, day
            )
            datepicker.show()
        }
        binding.add.setOnClickListener {
            val timelist = arrayListOf<Calendar>()
            if (time1 == null && time2 == null && time3 == null) {
                Toast.makeText(
                    this,
                    "Please select atleast one time to take medicine",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if (time1 != null) {
                    timelist.add(time1!!)
                }
                if (time2 != null) {
                    timelist.add(time2!!)
                }
                if (time3 != null) {
                    timelist.add(time3!!)
                }
            }
            if (startday == null || lastday == null) {
                Toast.makeText(this, "Select Dates", Toast.LENGTH_LONG).show()
            } else {
                val remList = arrayListOf<Reminder>()
                val datelist = arrayListOf<Calendar>()
                datelist.add(startday!!)
                val date = startday!!
                while (date.before(lastday)) {
                    date.add(Calendar.DAY_OF_MONTH, 1)
                    Log.i("reminder", "date" + SimpleDateFormat("dd/MM/yyyy").format(date.time))
                    Log.i("reminder", "Datelist size $datelist.size.toString()")
                    datelist.add(date)

                }
                for (dates in datelist) {
                    for (time in timelist) {
                        time.set(Calendar.SECOND, 0)
                        time.set(Calendar.MILLISECOND, 0)
                        val intent = Intent(this@SetReminder, AlarmReceiver::class.java)
                        intent.putExtra("medname", binding.textMedName.text.toString())
                        intent.putExtra("description", binding.descriptionInput.text.toString())
                        val pendingIntent = PendingIntent.getBroadcast(
                            this@SetReminder, 100, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        Log.i("reminder", "Alarm set up")
                        am.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            time.timeInMillis,
                            pendingIntent
                        )
                        Log.i("reminder", "date" + SimpleDateFormat("dd/MM/yyyy").format(dates.time))
                        Log.i("reminder", "time" + SimpleDateFormat("hh:mm a").format(time.time))
                        val remind = Reminder(
                            id = null,
                            medicineName = binding.textMedName.text.toString(),
                            description = binding.descriptionInput.text.toString(),
                            day = SimpleDateFormat("dd").format(dates.time),
                            month = SimpleDateFormat("MM").format(dates.time),
                            time = SimpleDateFormat("hh:mm a").format(time.time)
                        )
                        remList.add(remind)
                        Log.i("reminder", "remind added to list")
                    }
                }
                val intent = Intent(this, ReminderMain::class.java)
                intent.putExtra("remlist", remList as Serializable)
                Log.i("reminder", "mainactivity called")
                startActivity(intent)
            }
        }

        binding.cancel.setOnClickListener {
            val intent = Intent(this, ReminderMain::class.java)
            Log.i("reminder", "cancel button clicked")
            startActivity(intent)

        }
    }
}

package com.example.teacherkma.activity

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.example.teacherkma.R
import com.skydoves.powerspinner.PowerSpinnerView
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.teacherkma.config.Config
import com.example.teacherkma.model.DailyWorkModel
import com.example.teacherkma.model.SubjectModel
import com.example.teacherkma.utils.DatePickerFragment
import com.example.teacherkma.utils.MySingleton
import com.example.teacherkma.utils.TimePickerFragment
import com.example.teacherkma.utils.hideSoftKeyboard
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class AddDailyWorkActivity : AppCompatActivity() {
    var array = ArrayList<String>()
    var listSubject = ArrayList<SubjectModel>()
    var dailyWork = DailyWorkModel()
    var arrL = ArrayList<String>()

    private val REQUEST_CODE = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_daily_work)

        val psvSubject: PowerSpinnerView = findViewById(R.id.psvSubject)
        val psvLesson: PowerSpinnerView = findViewById(R.id.psvLesson)
        val clAddDailyWorkActivity: ConstraintLayout = findViewById(R.id.clAddDailyWorkActivity)
//        val llAddDailyWorkActivity: LinearLayout = findViewById(R.id.llAddDailyWorkActivity)
        val svAddDailyWorkActivity: ScrollView = findViewById(R.id.svAddDailyWorkActivity)
        val txtRoom: EditText = findViewById(R.id.txtRoom)
        val btnStartTime: Button = findViewById(R.id.btnStartTime)
        val btnEndTime: Button = findViewById(R.id.btnEndTime)
        val btnDate: Button = findViewById(R.id.btnDate)
        val btnAdd: Button = findViewById(R.id.btnAdd)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        //get info Teacher
        val prefs = getSharedPreferences("ID", MODE_PRIVATE)
        val teacher = prefs.getString(
            "teacher",
            "{}"
        ) //"No name defined" is the default value.
        val teacherInfo = JSONObject(teacher)

        dailyWork.teacherId = teacherInfo.getString("id")

        svAddDailyWorkActivity.setOnTouchListener(OnTouchListener { v, event ->
            psvSubject.dismiss()
            psvLesson.dismiss()
            hideSoftKeyboard()
            if (event != null && event.action == MotionEvent.ACTION_DOWN) {
                psvSubject.dismiss()
                hideSoftKeyboard()
            }
            false
        })

        clAddDailyWorkActivity.setOnClickListener {
            psvSubject.dismiss()
            hideSoftKeyboard()
        }

        val pickerStart = TimePickerFragment()
        val pickerEnd = TimePickerFragment()
        val pickerDate = DatePickerFragment()

        btnStartTime.setOnClickListener {
            pickerStart.show(supportFragmentManager, "timePickerStart")
        }

        btnEndTime.setOnClickListener {
            pickerEnd.show(supportFragmentManager, "timePickerEnd")
        }

        btnDate.setOnClickListener {
            pickerDate.show(supportFragmentManager, "datePicker")
        }

        pickerStart.getTime(btnStartTime, this)
        pickerEnd.getTime(btnEndTime, this)
        pickerDate.getDate(btnDate, this)

        //Get list Subject
        val url = Config.getValue() + "/Subject"

        val jsonRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                println("Response: %s".format(response.toString()))

                progressBar.visibility = View.INVISIBLE
                progressBar.stopNestedScroll()

                array = ArrayList<String>()
                listSubject = ArrayList()

                val response = JSONArray(response.toString())
                val length = response.length() - 1
                for (i in 0..length) {
//                    println(response.getJSONObject(i))
                    val obj = response.getJSONObject(i)
                    array.add(obj.getString("name"))

                    val sub = SubjectModel(
                        obj.getString("id"),
                        obj.getString("name"),
                        obj.getString("image"),
                        obj.getInt("numberOfLesson"),
                        obj.getString("type")
                    )
                    listSubject.add(sub)
                }
                psvSubject.setItems(array)
            },
            { err ->
                // TODO: Handle error
                println(err)
                progressBar.visibility = View.INVISIBLE
                progressBar.stopNestedScroll()
                Toast.makeText(this, "Không thể tải dữ liệu", Toast.LENGTH_LONG).show()
            }
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonRequest)

        for (i in 1..10) {
            arrL.add(i.toString())
        }

        psvLesson.setItems(arrL)

        psvSubject.setOnSpinnerItemSelectedListener(
            OnSpinnerItemSelectedListener<String> { _, _, index, item ->
                println(item + " " + index)
                hideSoftKeyboard()
                dailyWork.subjectName = item
                dailyWork.subjectId = listSubject[index].id
            }
        )

        psvLesson.setOnSpinnerItemSelectedListener(
            OnSpinnerItemSelectedListener<String> { _, _, index, item ->
                println(item)
                hideSoftKeyboard()
                dailyWork.lesson = item.toInt()
            }
        )

        btnAdd.setOnClickListener {
            dailyWork.date = btnDate.text.toString()
            dailyWork.room = txtRoom.text.toString()
            dailyWork.startTime = btnStartTime.text.toString()
            dailyWork.endTime = btnEndTime.text.toString()
            val animShake = AnimationUtils.loadAnimation(this, R.anim.shake)
            when {
                dailyWork.subjectName.isEmpty() -> {
                    psvSubject.startAnimation(animShake)
                }
                dailyWork.lesson == 0 -> {
                    psvLesson.startAnimation(animShake)
                }
                dailyWork.room.isEmpty() -> {
                    txtRoom.startAnimation(animShake)
                }
                dailyWork.startTime.isEmpty() -> {
                    btnStartTime.startAnimation(animShake)
                }
                dailyWork.endTime.isEmpty() -> {
                    btnEndTime.startAnimation(animShake)
                }
                dailyWork.date.isEmpty() -> {
                    btnDate.startAnimation(animShake)
                }
                else -> {
                    println(dailyWork)
                    println("Sending request")
                    progressBar.visibility = View.VISIBLE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        progressBar.setProgress(1, true)
                    }
                    val obj = JSONObject(dailyWork.toString())
                    val urlR = Config.getValue() + "/DailyWork/AddDailyWork"
                    val req = JsonObjectRequest(Request.Method.POST, urlR, obj, {
                        response ->
                        println("response: " + response.toString())
                        progressBar.visibility = View.INVISIBLE
                        progressBar.stopNestedScroll()
                        Toast.makeText(this, "Tạo thành công", Toast.LENGTH_SHORT).show()

                        Timer().schedule(1500){
                            //do something
                            finish()
                        }

//                        finish()
                    }, {
                        error ->
                        println("error: " + error.toString())
                        progressBar.visibility = View.INVISIBLE
                        progressBar.stopNestedScroll()
                        Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
                    })
                    MySingleton.getInstance(this).addToRequestQueue(req)
                }
            }
        }
    }


    fun View.hideKeyboard() {
        val inputMethodManager = context!!.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
    }


}


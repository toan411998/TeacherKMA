package com.example.teacherkma.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.teacherkma.R
import com.example.teacherkma.config.Config
import com.example.teacherkma.model.DailyWorkModel
import com.example.teacherkma.model.SubjectModel
import com.example.teacherkma.utils.DatePickerFragment
import com.example.teacherkma.utils.MySingleton
import com.example.teacherkma.utils.TimePickerFragment
import com.example.teacherkma.utils.hideSoftKeyboard
import com.google.gson.JsonObject
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class DetailDailyWorkActivity : AppCompatActivity() {

    var array = ArrayList<String>()
    var listSubject = ArrayList<SubjectModel>()
    var dailyWork = DailyWorkModel()
    var arrL = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_daily_work)

        //get info Teacher
        val prefs = getSharedPreferences("ID", MODE_PRIVATE)
        val teacher = prefs.getString(
            "teacher",
            "{}"
        ) //"No name defined" is the default value.
        val teacherInfo = JSONObject(teacher)

        // get dailyWork
        val work = intent.getStringExtra("dailyWork")
        val json = JSONObject(work)
        println(json)
        println(work)

        val psvSubject: PowerSpinnerView = findViewById(R.id.psvSubject)
        val psvLesson: PowerSpinnerView = findViewById(R.id.psvLesson)
        val clDetailDailyWorkActivity: ConstraintLayout = findViewById(R.id.clDetailDailyWorkActivity)
//        val llAddDailyWorkActivity: LinearLayout = findViewById(R.id.llAddDailyWorkActivity)
        val svDetailDailyWorkActivity: ScrollView = findViewById(R.id.svDetailDailyWorkActivity)
        val txtRoom: EditText = findViewById(R.id.txtRoom)
        val btnStartTime: Button = findViewById(R.id.btnStartTime)
        val btnEndTime: Button = findViewById(R.id.btnEndTime)
        val btnDate: Button = findViewById(R.id.btnDate)
        val btnSave: Button = findViewById(R.id.btnSave)
        val btnConfirm: Button = findViewById(R.id.btnConfirm)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        val role = teacherInfo.getString("role")
        if (role.equals("manager")) {
            btnSave.isVisible = false
            btnConfirm.isVisible = true
        }
        else {
            btnSave.isVisible = true
            btnConfirm.isVisible = false
        }

        dailyWork.id = json.getString("id")
        dailyWork.teacherId = teacherInfo.getString("id")
        dailyWork.subjectId = json.getString("subjectId")
        dailyWork.subjectName = json.getString("subjectName")
        dailyWork.lesson = json.getInt("lesson")

        svDetailDailyWorkActivity.setOnTouchListener(View.OnTouchListener { v, event ->
            psvSubject.dismiss()
            psvLesson.dismiss()
            hideSoftKeyboard()
            if (event != null && event.action == MotionEvent.ACTION_DOWN) {
                psvSubject.dismiss()
                hideSoftKeyboard()
            }
            false
        })

        clDetailDailyWorkActivity.setOnClickListener {
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
        val url = Config.getValue() + "/Subject/GetTeachSubject?teacherId=" + teacherInfo.getString("id")
        println(url)

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

        psvSubject.text = json.getString("subjectName")
        psvSubject.setOnSpinnerItemSelectedListener(
            OnSpinnerItemSelectedListener<String> { _, _, index, item ->
                println(item + " " + index)
                hideSoftKeyboard()
                dailyWork.subjectName = item
                dailyWork.subjectId = listSubject[index].id
            }
        )

        psvLesson.text = json.getString("lesson")
        psvLesson.setOnSpinnerItemSelectedListener(
            OnSpinnerItemSelectedListener<String> { _, _, index, item ->
                println(item)
                hideSoftKeyboard()
                dailyWork.lesson = item.toInt()
            }
        )

        btnDate.text = json.getString("date").toString()
        txtRoom.setText(json.getString("room"))
        btnStartTime.text = json.getString("startTime").toString()
        btnEndTime.text = json.getString("endTime").toString()
        dailyWork.date = btnDate.text.toString()
        dailyWork.room = txtRoom.text.toString()
        dailyWork.startTime = btnStartTime.text.toString()
        dailyWork.endTime = btnEndTime.text.toString()

        btnSave.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progressBar.setProgress(1, true)
            }
            val obj = JSONObject(dailyWork.toString())
            val urlR = Config.getValue() + "/DailyWork/EditDailyWork"
            val req = JsonObjectRequest(Request.Method.PUT, urlR, obj, {
                    response ->
                println("response: " + response.toString())
                progressBar.visibility = View.INVISIBLE
                progressBar.stopNestedScroll()
                Toast.makeText(this, "Lưu thành công", Toast.LENGTH_SHORT).show()

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

        btnConfirm.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progressBar.setProgress(1, true)
            }
            dailyWork.state = 1
            dailyWork.teacherId = json.getString("teacherId")
            val obj = JSONObject(dailyWork.toString())
            val urlR = Config.getValue() + "/DailyWork/EditDailyWork"
            val req = JsonObjectRequest(Request.Method.PUT, urlR, obj, {
                    response ->
                println("response: " + response.toString())
                progressBar.visibility = View.INVISIBLE
                progressBar.stopNestedScroll()
                Toast.makeText(this, "Duyệt thành công", Toast.LENGTH_SHORT).show()

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
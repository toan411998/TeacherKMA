package com.example.teacherkma.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.teacherkma.MainActivity
import com.example.teacherkma.MainManagerActivity
import com.example.teacherkma.R
import com.example.teacherkma.config.Config
import com.example.teacherkma.model.TeacherModel
import com.example.teacherkma.utils.MySingleton
import com.example.teacherkma.utils.hideSoftKeyboard
import com.google.gson.Gson
import com.khaledahmedelsayed.pinview.PinView
import org.json.JSONObject

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        getSupportActionBar()?.setTitle("Đổi mật khẩu")

        //get info Teacher
        val prefs = getSharedPreferences("ID", AppCompatActivity.MODE_PRIVATE)
        val teacher = prefs.getString(
            "teacher",
            "{}"
        ) //"No name defined" is the default value.
        val teacherInfo = JSONObject(teacher)

        val btnConfirm: Button = findViewById(R.id.btnConfirm)
        val pinView: PinView = findViewById(R.id.pinView)
        val llBody: LinearLayout = findViewById(R.id.llBody)

        val txtNewPassword: EditText = findViewById(R.id.txtNewPassword)
        val txtPassword: EditText = findViewById(R.id.txtPassword)
        val txtConfirmPassword: EditText = findViewById(R.id.txtConfirmPassword)

        val animShake = AnimationUtils.loadAnimation(this, R.anim.shake)

        btnConfirm.setOnClickListener {
            if (txtPassword.text.isNullOrEmpty()) {
                txtPassword.startAnimation(animShake)
            }
            else if (txtNewPassword.text.isNullOrEmpty()) {
                txtNewPassword.startAnimation(animShake)
            }
            else if (txtConfirmPassword.text.isNullOrEmpty() || !txtConfirmPassword.text.toString().equals(txtNewPassword.text.toString())) {
                txtConfirmPassword.startAnimation(animShake)
            }
            else {
                hideSoftKeyboard()
                pinView.visibility = View.VISIBLE
                llBody.visibility = View.INVISIBLE
            }
        }

        pinView.setOnCompletedListener = { pinCode ->
            if(pinCode == "1234") {
                Toast.makeText(this, "Mã PIN đúng", Toast.LENGTH_SHORT).show()
                pinView.visibility = View.INVISIBLE
                llBody.visibility = View.VISIBLE
                val url = Config.getValue() + "/Teacher/EditTeacher?oldPass=" + teacherInfo.getString("password") + "&newPass=" + txtNewPassword.text
                println(url)

                val gson: Gson = Gson()
                val obj = gson.fromJson(teacher, TeacherModel::class.java)
                val json : JSONObject = JSONObject(teacher)

                val jsonRequest = JsonObjectRequest(
                    Request.Method.PUT, url, json,
                    { response ->
                        println("Response: %s".format(response.toString()))


                    },
                    { err ->
                        // TODO: Handle error
                        println(err)

                        Toast.makeText(this, "Mật khẩu cũ không đúng!", Toast.LENGTH_SHORT).show()
                    }
                )

                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(this).addToRequestQueue(jsonRequest)
            }
            else
                Toast.makeText(this, "Mã PIN sai", Toast.LENGTH_SHORT).show()

            pinView.clearPin()
        }
    }
}
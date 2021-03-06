package com.example.teacherkma.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.teacherkma.MainActivity
import com.example.teacherkma.R
import com.example.teacherkma.config.Config
import com.example.teacherkma.model.TeacherModel
import com.example.teacherkma.utils.MySingleton
import com.example.teacherkma.utils.hideSoftKeyboard
import org.json.JSONArray
import org.json.JSONObject
import android.content.SharedPreferences
import com.example.teacherkma.MainManagerActivity


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val clLoginActivity: ConstraintLayout = findViewById(R.id.clLoginActivity)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        val txtUsername: EditText = findViewById(R.id.txtUsername)
        val txtPassword: EditText = findViewById(R.id.txtPassword)

        clLoginActivity.setOnClickListener {
            hideSoftKeyboard()
        }

        btnLogin.setOnClickListener {

            println(txtUsername.text)
            println(txtPassword.text)

            val animShake = AnimationUtils.loadAnimation(this, R.anim.shake)

            when {
                txtUsername.text.isNullOrEmpty() -> {
                    txtUsername.startAnimation(animShake)
                    Toast.makeText(this, "T??i kho???n kh??ng ???????c b??? tr???ng", Toast.LENGTH_SHORT).show()
                }
                txtPassword.text.isNullOrEmpty() -> {
                    txtPassword.startAnimation(animShake)
                    Toast.makeText(this, "M???t kh???u kh??ng ???????c b??? tr???ng", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    progressBar.visibility = View.VISIBLE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        progressBar.setProgress(1, true)
                    }
                    val url = Config.getValue() + "/Teacher/GetTeacher?username=" + txtUsername.text + "&password=" + txtPassword.text
                    println(url)
                    val jsonRequest = JsonObjectRequest(
                        Request.Method.GET, url, null,
                        { response ->
                            println("Response: %s".format(response.toString()))
                            progressBar.visibility = View.INVISIBLE
                            progressBar.stopNestedScroll()

                            val obj = JSONObject(response.toString())
                            var numOfStudyLesson = 0
                            if (obj.getString("numOfStudyLesson").isNullOrEmpty()) {
                                numOfStudyLesson = 0
                            }

                            var numOfTeachingLesson = 0
                            if (obj.getString("numOfTeachingLesson").isNullOrEmpty()) {
                                numOfTeachingLesson = 0
                            }
                            val teacher = TeacherModel(
                                obj.getString("id"),
                                obj.getString("username"),
                                obj.getString("name"),
                                obj.getString("gender"),
                                obj.getString("majorsId"),
                                obj.getString("image"),
                                numOfStudyLesson,
                                numOfTeachingLesson,
                                obj.getString("date"),
                                obj.getString("address"),
                                obj.getString("phone"),
                                obj.getString("mail"),
                                obj.getString("password"),
                                obj.getString("role")
                            )

                            val sharedPref = getSharedPreferences("ID", MODE_PRIVATE).edit()
                            sharedPref.putString("teacher", teacher.toString())
                            sharedPref.commit()

                            val prefs = getSharedPreferences("ID", MODE_PRIVATE)
                            val user = prefs.getString(
                                "teacher",
                                "{}"
                            ) //"No name defined" is the default value.

                            println(user)
                            val t = JSONObject(user)
                            println(t.getString("id"))

                            if (teacher.role == "manager") {
                                val intent = Intent(this, MainManagerActivity::class.java)
                                startActivity(intent)
                            }
                            else {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }

                        },
                        { err ->
                            // TODO: Handle error
                            println(err)
                            progressBar.visibility = View.INVISIBLE
                            progressBar.stopNestedScroll()

                            Toast.makeText(this, "T??i kho???n ho???c m???t kh???u kh??ng ????ng!", Toast.LENGTH_SHORT).show()
                        }
                    )

                    // Access the RequestQueue through your singleton class.
                    MySingleton.getInstance(this).addToRequestQueue(jsonRequest)
                }
            }

        }

    }
}
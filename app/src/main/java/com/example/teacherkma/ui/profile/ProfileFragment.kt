package com.example.teacherkma.ui.profile

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.teacherkma.MainManagerActivity
import com.example.teacherkma.R
import com.example.teacherkma.activity.ChangePasswordActivity
import com.example.teacherkma.databinding.FragmentNotificationsBinding
import com.example.teacherkma.databinding.ProfileFragmentBinding
import com.example.teacherkma.ui.notifications.NotificationsViewModel
import org.json.JSONObject

class ProfileFragment : Fragment() {

    val REQUEST_IMAGE_CAPTURE = 1

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: ProfileFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //get info Teacher
        val prefs = this.requireActivity().getSharedPreferences("ID", AppCompatActivity.MODE_PRIVATE)
        val teacher = prefs.getString(
            "teacher",
            "{}"
        ) //"No name defined" is the default value.
        val teacherInfo = JSONObject(teacher)

        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var b = binding.imageButton2
        b.setOnClickListener {
            dispatchTakePictureIntent()
        }

        val txtName: TextView = binding.txtName
        txtName.text = teacherInfo.getString("name")

        val txtGender: TextView = binding.txtGender
        txtGender.text = teacherInfo.getString("gender")

        val btnChangePassword: Button = binding.btnChangePassword

        btnChangePassword.setOnClickListener {
            val intent = Intent(context, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val imageView3: ImageView = binding.imageView3
            imageView3.setImageBitmap(imageBitmap)
        }
    }

}
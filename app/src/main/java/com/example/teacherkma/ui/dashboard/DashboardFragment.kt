package com.example.teacherkma.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.teacherkma.R
import com.example.teacherkma.databinding.FragmentDashboardBinding
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.example.teacherkma.activity.AddDailyWorkActivity
import com.example.teacherkma.activity.DetailActivity
import com.example.teacherkma.adapter.CustomAdapter
import com.example.teacherkma.adapter.HeaderAdapter
import com.example.teacherkma.config.Config
import com.example.teacherkma.model.DailyWorkModel
import com.example.teacherkma.utils.MySingleton
import org.json.JSONArray
import org.json.JSONObject

class DashboardFragment : Fragment(), View.OnClickListener {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    private val REQUEST_CODE = 5

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var dailyWorkList : ArrayList<DailyWorkModel> = ArrayList()

    var headerAdapter = HeaderAdapter(context)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        //get info Teacher
        val prefs = this.requireActivity().getSharedPreferences("ID", AppCompatActivity.MODE_PRIVATE)
        val teacher = prefs.getString(
            "teacher",
            "{}"
        ) //"No name defined" is the default value.
        val teacherInfo = JSONObject(teacher)

        val root: View = binding.root
        val textView: TextView = binding.textDashboard
        val radioGroup: RadioGroup = binding.radioGroup

        val progressBar: ProgressBar = binding.progressBar

        val recyclerviewList: RecyclerView = binding.recyclerviewList

        val imageButtonAdd: ImageButton = binding.imageButtonAdd

        // this creates a vertical layout Manager
        recyclerviewList.layoutManager = LinearLayoutManager(context)

        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        headerAdapter = HeaderAdapter(context)
        headerAdapter.HeaderAdapter(context)

        headerAdapter.onClickListener = object : HeaderAdapter.DetailsAdapterListener {
            override fun onClickSearch(v: View?) {
                println(headerAdapter.isStudy)
                println(headerAdapter.startDate)
                println(headerAdapter.endDate)
            }

        }


        progressBar.isVisible = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress(1, true)
        }

        radioGroup.check(R.id.radioButtonClass)

        radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radioButtonClass -> {
                        println("Class")
                    }
                    R.id.radioButtonStudy -> {
                        println("Study")
                    }
                }
            }
        )

        val customAdapter = CustomAdapter { myModel -> adapterOnClick(myModel) }

//        val customAdapter = CustomAdapter(list)

        val concatAdapter = ConcatAdapter(headerAdapter, customAdapter)
        recyclerviewList.adapter = concatAdapter

        val url = Config.getValue() + "/DailyWork/GetDailyWorkByTeacherId?teacherId=" + teacherInfo.getString("id")

        println(url)

        val jsonRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                println("Response: %s".format(response.toString()))
                val jsonArr = JSONArray(response.toString())
                val n = jsonArr.length() - 1
                dailyWorkList = ArrayList()
                for (item in 0..n) {
                    val obj = jsonArr.getJSONObject(item)
                    val m = DailyWorkModel(
                        obj.getString("id"),
                        obj.getString("teacherId"),
                        obj.getString("subjectId"),
                        obj.getString("subjectName"),
                        obj.getString("date"),
                        obj.getInt("lesson"),
                        obj.getString("room"),
                        obj.getInt("state"),
                        obj.getString("startTime"),
                        obj.getString("endTime"),
                    )
                    dailyWorkList.add(m)
                }
                customAdapter.submitList(dailyWorkList)
                progressBar.stopNestedScroll()
                progressBar.visibility = View.INVISIBLE
            },
            { err ->
                // TODO: Handle error
                println(err)
                progressBar.stopNestedScroll()
                progressBar.visibility = View.INVISIBLE
            }
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(requireActivity().applicationContext).addToRequestQueue(jsonRequest)

        //Add daily work
        imageButtonAdd.setOnClickListener {
            println("Click add")
            val intent = Intent (activity, AddDailyWorkActivity::class.java)
            activity?.startActivity(intent)
        }

        return root
    }

    override fun onClick(view: View) {
        when (view.id) {

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    /* Opens Activity when RecyclerView item is clicked. */
    private fun adapterOnClick(flower: DailyWorkModel) {
        val intent = Intent (activity, DetailActivity::class.java)
        activity?.startActivityFromFragment(this, intent, REQUEST_CODE)
    }

    override fun onResume() {
        super.onResume()
        println("hihi")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        println("pass back")

        /* Inserts flower into viewModel. */
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val flowerName = data.getStringExtra("")

            }
        }
    }
}
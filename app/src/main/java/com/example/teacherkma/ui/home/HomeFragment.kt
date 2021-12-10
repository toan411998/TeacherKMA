package com.example.teacherkma.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.teacherkma.R
import com.example.teacherkma.databinding.FragmentHomeBinding
import java.util.ArrayList

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        val imageSlider: ImageSlider = binding.imageSlider
        val imgList = ArrayList<SlideModel>()
        imgList.add(SlideModel(R.drawable.kma1, ScaleTypes.FIT))
        imgList.add(SlideModel(R.drawable.kma2, ScaleTypes.FIT))
        imgList.add(SlideModel(R.drawable.kma3, ScaleTypes.FIT))
        imgList.add(SlideModel(R.drawable.kma4, ScaleTypes.FIT))

        imageSlider.setImageList(imgList, ScaleTypes.FIT)
        imageSlider.startSliding(3000)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
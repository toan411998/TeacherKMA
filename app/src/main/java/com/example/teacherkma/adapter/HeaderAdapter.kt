package com.example.teacherkma.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.teacherkma.R
import com.example.teacherkma.utils.DatePickerFragment
import com.example.teacherkma.utils.getDaysAgo
import java.text.SimpleDateFormat
import java.util.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.teacherkma.ui.dashboard.DashboardFragment


class HeaderAdapter(context: Context?) : RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {

    private var flowerCount: Int = 0
    private var context: Context? = null

    var onClickListener: DetailsAdapterListener? = null

    public var isStudy: Boolean = false
    public var startDate: String = ""
    public var endDate: String = ""

    fun HeaderAdapter(context: Context?, isStudy: Boolean) {
        this.context = context
        this.isStudy = isStudy
    }

    /* ViewHolder for displaying header. */
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view){
//        private val flowerNumberTextView: TextView = itemView.findViewById(R.id.flower_number_text)
        
        val radioButtonTeach: RadioButton = itemView.findViewById(R.id.radioButtonTeach)
        val radioButtonStudy: RadioButton = itemView.findViewById(R.id.radioButtonStudy)
        val buttonStartDate: Button = itemView.findViewById(R.id.buttonStartDate)
        val buttonEndDate: Button = itemView.findViewById(R.id.buttonEndDate)
        val buttonSearch: Button = itemView.findViewById(R.id.buttonSearch)

        fun bind(flowerCount: Int) {
//            flowerNumberTextView.text = flowerCount.toString()
        }
    }

    /* Inflates view and returns HeaderViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.header_item, parent, false)
        return HeaderViewHolder(view)
    }

    /* Binds number of flowers to the header. */
    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(flowerCount)
        if (isStudy) {
            holder.radioButtonStudy.isChecked = true
        } else {
            holder.radioButtonTeach.isChecked = true
        }
        holder.radioButtonTeach.setOnClickListener {
//            onClickListener?.onClickTeach(it)
            isStudy = false
        }
        holder.radioButtonStudy.setOnClickListener {
//            onClickListener?.onClickStudy(it)
            isStudy = true
        }

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = sdf.format(Date())
        holder.buttonEndDate.text = currentDate.toString()
        val beforeDate = sdf.format(getDaysAgo(7))
        holder.buttonStartDate.text = beforeDate.toString()

        holder.buttonStartDate.setOnClickListener {
            val pickerStartDate = DatePickerFragment()
            val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            pickerStartDate.show(manager, "startDate")
            pickerStartDate.getDate(holder.buttonStartDate, context as AppCompatActivity)
        }

        holder.buttonEndDate.setOnClickListener {
            val pickerStartDate = DatePickerFragment()
            val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            pickerStartDate.show(manager, "endDate")
            pickerStartDate.getDate(holder.buttonEndDate, context as AppCompatActivity)
        }

        holder.buttonSearch.setOnClickListener {
            startDate = holder.buttonStartDate.text.toString()
            endDate = holder.buttonEndDate.text.toString()
            onClickListener?.onClickSearch(it)
        }
    }

    /* Returns number of items, since there is only one item in the header return one  */
    override fun getItemCount(): Int {
        return 1
    }

    /* Updates header to display number of flowers when a flower is added or subtracted. */
    fun updateFlowerCount(updatedFlowerCount: Int) {
        flowerCount = updatedFlowerCount
        notifyDataSetChanged()
    }

    //region Interface Details listener
    interface DetailsAdapterListener {
//        fun onClickStudy(v: View?)
//        fun onClickTeach(v: View?)
        fun onClickSearch(v: View?)
    }
    //endregion
}
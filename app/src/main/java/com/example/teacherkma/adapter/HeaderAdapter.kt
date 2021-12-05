package com.example.teacherkma.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.teacherkma.R
import java.util.ArrayList

class HeaderAdapter: RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {

    private var flowerCount: Int = 0
    var onClickListener: DetailsAdapterListener? = null

//    fun HeaderAdapter(
//        listener: DetailsAdapterListener?
//    ) {
//        onClickListener = listener
//    }

    /* ViewHolder for displaying header. */
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view){
//        private val flowerNumberTextView: TextView = itemView.findViewById(R.id.flower_number_text)
        
        val radioButtonTeach: RadioButton = itemView.findViewById(R.id.radioButtonTeach)
        val radioButtonStudy: RadioButton = itemView.findViewById(R.id.radioButtonStudy)
        val buttonStartTime: Button = itemView.findViewById(R.id.buttonStartTime)
        val buttonEndTime: Button = itemView.findViewById(R.id.buttonEndTime)
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
        holder.radioButtonTeach.setOnClickListener {
            onClickListener?.onClickStudy(it)
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
        fun onClickStudy(v: View?)
//        fun onClickDes(v: View?, position: Int)
    }
    //endregion
}
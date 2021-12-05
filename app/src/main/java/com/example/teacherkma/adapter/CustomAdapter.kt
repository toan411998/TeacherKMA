package com.example.teacherkma.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.teacherkma.R
import com.example.teacherkma.model.DailyWorkModel


class CustomAdapter(private val onClick: (DailyWorkModel) -> Unit) :
    ListAdapter<DailyWorkModel, CustomAdapter.CustomViewHolder>(DailyWorkModelDiffCallback) {

    /* ViewHolder for DailyWorkModel, takes in the inflated view and the onClick behavior. */
    class CustomViewHolder(itemView: View, val onClick: (DailyWorkModel) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val txtName: TextView = itemView.findViewById(R.id.textViewName)
        private val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        private val txtRoom: TextView = itemView.findViewById(R.id.txtRoom)
        private val txtTime: TextView = itemView.findViewById(R.id.txtTime)
        private var currentDailyWorkModel: DailyWorkModel? = null

        init {
            itemView.setOnClickListener {
                currentDailyWorkModel?.let {
                    onClick(it)
                }
            }
        }

        /* Bind DailyWorkModel name and image. */
        fun bind(DailyWorkModel: DailyWorkModel) {
            currentDailyWorkModel = DailyWorkModel

            txtName.text = DailyWorkModel.subjectName
            txtDate.text = DailyWorkModel.date
            txtRoom.text = DailyWorkModel.room
            txtTime.text = DailyWorkModel.startTime + " - " + DailyWorkModel.endTime
        }
    }

    /* Creates and inflates view and return DailyWorkModelViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_item, parent, false)
        return CustomViewHolder(view, onClick)
    }

    /* Gets current DailyWorkModel and uses it to bind view. */
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val DailyWorkModel = getItem(position)
        holder.bind(DailyWorkModel)
    }
}

//class CustomAdapter(private val mList: List<DailyWorkModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
//
//    // create new views
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        // inflates the card_view_design view
//        // that is used to hold list item
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.my_item, parent, false)
//
//        return ViewHolder(view)
//    }
//
//    // binds the list items to a view
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        val ItemsViewModel = mList[position]
//        // sets the text to the textview from our itemHolder class
//        holder.textView.text = ItemsViewModel.name
//
//    }
//
//    // return the number of the items in the list
//    override fun getItemCount(): Int {
//        return mList.size
//    }
//
//    // Holds the views for adding it to image and text
//    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
//        val textView: TextView = itemView.findViewById(R.id.txtName)
//    }
//}

object DailyWorkModelDiffCallback : DiffUtil.ItemCallback<DailyWorkModel>() {
    override fun areItemsTheSame(oldItem: DailyWorkModel, newItem: DailyWorkModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DailyWorkModel, newItem: DailyWorkModel): Boolean {
        return oldItem.id == newItem.id
    }
}
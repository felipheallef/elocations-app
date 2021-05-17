package com.felipheallef.elocations.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.felipheallef.elocations.databinding.ListItemPictureBinding
import com.google.android.material.snackbar.Snackbar

const val TAG = "PictureItemAdapter"

class PictureItemAdapter(
    var pictures: MutableList<Bitmap>) :
    RecyclerView.Adapter<PictureItemAdapter.ViewHolder>() {

    lateinit var context: Context
    private lateinit var binding: ListItemPictureBinding
    private var index = 0
    val editable = false

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        init {
//            // Define click listener for the ViewHolder's View.
//        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        context = viewGroup.context
        binding = ListItemPictureBinding.inflate(LayoutInflater.from(viewGroup.context),
            viewGroup, false)
        return ViewHolder(binding.root)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        index = position
        binding.fabRemove.hide()

        if (editable) {
            binding.fabRemove.show()
            binding.fabRemove.setOnClickListener {
                Log.d(TAG, "Position: $position")
                try {
                    pictures.removeAt(position)
                    notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
//            pictures.removeAt(position)
            }
        }

        Glide
            .with(context)
            .load(pictures[position])
            .centerCrop()
//            .placeholder(R.drawable.loading_spinner)
            .into(binding.picture)

    }

    fun clear() {
        pictures.clear()
        notifyDataSetChanged()
    }

    fun add(bitmap: Bitmap) {
        pictures.add(bitmap)
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(list: MutableList<Bitmap>) {
        pictures.addAll(list)
        notifyDataSetChanged()
    }

    // Return the size of your data set (invoked by the layout manager)
    override fun getItemCount() = pictures.size

}

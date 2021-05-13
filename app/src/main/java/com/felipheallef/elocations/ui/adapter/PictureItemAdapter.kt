package com.felipheallef.elocations.ui.adapter

import android.graphics.Bitmap
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.felipheallef.elocations.databinding.ListItemPictureBinding

class PictureItemAdapter(
    private val pictures: List<Bitmap>) :
    RecyclerView.Adapter<PictureItemAdapter.ViewHolder>() {

    lateinit var context: Context
    private lateinit var binding: ListItemPictureBinding

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val textView: TextView = view.findViewById(R.id.tv_film_title)
//        val ivFilmCover: ImageView = view.findViewById(R.id.iv_film_cover)

        init {
            // Define click listener for the ViewHolder's View.
        }
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

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        binding.picture.setOnClickListener {
            TODO("Implement click")
        }

        Glide
            .with(context)
            .load(pictures[position])
            .centerCrop()
//            .placeholder(R.drawable.loading_spinner)
            .into(binding.picture)

    }

    // Return the size of your data set (invoked by the layout manager)
    override fun getItemCount() = pictures.size

}

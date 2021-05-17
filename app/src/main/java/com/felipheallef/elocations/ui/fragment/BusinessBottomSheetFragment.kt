package com.felipheallef.elocations.ui.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.felipheallef.elocations.Application
import com.felipheallef.elocations.R
import com.felipheallef.elocations.data.model.Business
import com.felipheallef.elocations.databinding.BottomsheetBusinessDetailsBinding
import com.felipheallef.elocations.ui.activity.CreateNewActivity
import com.felipheallef.elocations.ui.activity.MainActivity
import com.felipheallef.elocations.ui.adapter.PictureItemAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.io.FileDescriptor
import java.io.IOException

const val TAG = "BusinessBottomSheet"

class BusinessBottomSheetFragment() : BottomSheetDialogFragment() {

    lateinit var business: Business
    lateinit var storageDir: File
    private lateinit var binding: BottomsheetBusinessDetailsBinding

    override fun getTheme(): Int {
        return R.style.Theme_ELocations_BottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val view = inflater.inflate(R.layout.bottomsheet_business_details, container, false)
        binding = BottomsheetBusinessDetailsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(this::storageDir.isInitialized) {

            val pictures = mutableListOf<Bitmap>()

            storageDir.listFiles()?.forEach {

                if (it.name.startsWith(business.id.toString())) {
                    Log.d(TAG, it.name)
                    val bitmap = getBitmapFromUri(Uri.fromFile(it))
                    pictures.add(bitmap)
                }
            }

            binding.listPictures.setHasFixedSize(true)
            binding.listPictures.layoutManager = LinearLayoutManager(
                activity?.applicationContext,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            val adapter = PictureItemAdapter(pictures)
            binding.listPictures.adapter = adapter
        }

        setupBusinessInfo()
    }

    @Throws(IOException::class)
    fun getBitmapFromUri(uri: Uri?): Bitmap {
        val parcelFileDescriptor: ParcelFileDescriptor? = activity?.contentResolver?.openFileDescriptor(uri!!, "r")
        val fileDescriptor: FileDescriptor? = parcelFileDescriptor?.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        return image
    }

    private fun setupBusinessInfo() {

        binding.tvBusinessName.text = business.name
        binding.tvBusinessDescription.text = business.description +
                "\n\nTelefone: ${business.number}" +
                "\n\nCategoria: ${business.category}"

    }

    companion object {

        fun getInstance(business: Business, file: File?): BusinessBottomSheetFragment {
            val fragment = BusinessBottomSheetFragment()
            fragment.business = business

            if(file != null)
                fragment.storageDir = file

            return fragment
        }

    }
}
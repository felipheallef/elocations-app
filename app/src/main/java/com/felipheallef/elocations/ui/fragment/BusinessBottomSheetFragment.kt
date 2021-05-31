package com.felipheallef.elocations.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.felipheallef.elocations.Application
import com.felipheallef.elocations.R
import com.felipheallef.elocations.data.entity.Business
import com.felipheallef.elocations.databinding.BottomsheetBusinessDetailsBinding
import com.felipheallef.elocations.ui.activity.EditBusinessActivity
import com.felipheallef.elocations.ui.adapter.PictureItemAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import java.io.File
import java.io.FileDescriptor
import java.io.IOException

class BusinessBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var business: Business
    lateinit var storageDir: File
    private lateinit var binding: BottomsheetBusinessDetailsBinding
    var doAfterDeleted: () -> Unit = {}
    var doAfterResult: () -> Unit = {}

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
        setupActionButtons()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK) {
            dismiss()
            doAfterResult.invoke()
        }
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
//        binding.tvBusinessDescription.text = business.description +
//                "\n\nTelefone: ${business.number}" +
//                "\n\nCategoria: ${business.category}"
        binding.tvBusinessDescription.text = getString(R.string.text_display_business_description)
            .format(business.description, business.number, business.category)

    }

    private fun setupActionButtons() {

        // Delete button
        binding.btnDelete.setOnClickListener {
            val deleted = Application.database?.businessDao()?.delete(business)

            // check if deleted successful
            if (deleted != 0) {
                business = Application.database?.businessDao()?.findById(business.id)!!
                doAfterDeleted.invoke()
                dismiss()
            } else {
                Toast.makeText(activity?.applicationContext, "Erro ao tentar excluir o registro.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.btnEdit.setOnClickListener {
            val extras = Bundle()
            val businessStr = Gson().toJson(this.business)
            extras.putString("business", businessStr)

            val intent = Intent(activity?.applicationContext, EditBusinessActivity::class.java).apply {
                putExtras(extras)
            }

//            activity?.startActivityFromFragment(this@BusinessBottomSheetFragment, intent, 100)
            startActivityForResult(intent, 100)
        }
    }

    companion object {

        const val TAG = "BusinessBottomSheet"

        fun getInstance(business: Business, file: File?): BusinessBottomSheetFragment {
            val fragment = BusinessBottomSheetFragment()
            fragment.business = business

            if(file != null)
                fragment.storageDir = file

            return fragment
        }

    }
}
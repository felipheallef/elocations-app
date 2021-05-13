package com.felipheallef.elocations.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.felipheallef.elocations.R
import com.felipheallef.elocations.data.model.Business
import com.felipheallef.elocations.databinding.ActivityCreateNewBinding
import com.felipheallef.elocations.ui.adapter.PictureItemAdapter
import com.felipheallef.elocations.ui.model.BusinessesViewModel
import java.io.FileDescriptor
import java.io.IOException

class CreateNewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNewBinding
    private val pictures = mutableListOf<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val model = BusinessesViewModel(applicationContext)

        val items = listOf("Loja de departamento", "Universidade", "Shopping", "Aeroporto")
        val adapter = ArrayAdapter(applicationContext, R.layout.list_item, items)
        (binding.fieldCategory.editText as? AutoCompleteTextView)?.setAdapter(adapter)


        binding.btnMore.setOnClickListener {
            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickIntent, 100)
        }

        loadPictures()

        binding.btnSave.setOnClickListener {
            val name = binding.fieldName.editText?.text.toString()
            val description = binding.fieldDescription.editText?.text.toString()
            val category = binding.fieldCategory.editText?.text.toString()
            val number = binding.fieldNumber.editText?.text.toString()
            val latitude = intent.extras?.getDouble("latitude")
            val longitude = intent.extras?.getDouble("latitude")
            val data = Business(name, description, number, category, latitude!!, longitude!!)

            model.add(data)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        pictures.add(getBitmapFromUri(data?.data))
        loadPictures()
    }

    private fun loadPictures() {
        binding.listPictures.setHasFixedSize(true)
        binding.listPictures.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.listPictures.adapter = PictureItemAdapter(pictures)
//        rvFilms.adapter = FilmCategoryItemAdapter(filmsCategory, fragmentManager!!)
    }

    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri?): Bitmap {
        val parcelFileDescriptor: ParcelFileDescriptor? = contentResolver.openFileDescriptor(uri!!, "r")
        val fileDescriptor: FileDescriptor? = parcelFileDescriptor?.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        return image
    }

}
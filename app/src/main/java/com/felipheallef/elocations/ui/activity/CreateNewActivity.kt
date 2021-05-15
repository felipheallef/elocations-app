package com.felipheallef.elocations.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.felipheallef.elocations.R
import com.felipheallef.elocations.data.model.Business
import com.felipheallef.elocations.databinding.ActivityCreateNewBinding
import com.felipheallef.elocations.ui.adapter.PictureItemAdapter
import com.felipheallef.elocations.ui.model.BusinessesViewModel
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.util.*

class CreateNewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNewBinding
    private val pictures = mutableListOf<Bitmap>()
    private lateinit var currentPhotoPath: String

    private lateinit var name: String
    private lateinit var description: String
    private lateinit var category: String
    private lateinit var number: String

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
            name = binding.fieldName.editText?.text.toString()
            description = binding.fieldDescription.editText?.text.toString()
            val category = binding.fieldCategory.editText?.text.toString()
            number = binding.fieldNumber.editText?.text.toString()
            val latitude = intent.extras?.getDouble("latitude")
            val longitude = intent.extras?.getDouble("longitude")
            val data = Business(name, description, number, category, latitude!!, longitude!!)

            if(validateData()) {
                val id = model.add(data)

                if(pictures.isNotEmpty()) {
                    pictures.forEachIndexed { index, bitmap ->
                        val fOut = FileOutputStream(createImageFile(id!!, index))

                        bitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            85,
                            fOut
                        ) // saving the Bitmap to a file compressed as a JPEG with 85% compression rate

                        fOut.flush() // Not really required
                        fOut.close() // do not forget to close the stream
                    }
                }

                setResult(RESULT_OK)
                finish();

            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        pictures.add(getBitmapFromUri(data?.data))
        loadPictures()
    }

    private fun validateData(): Boolean {
        binding.fieldName.error = ""
        binding.fieldDescription.error = ""
        binding.fieldNumber.error = ""
        return when {
            name.isEmpty() -> {
                binding.fieldName.error = "Digite o nome do estabelecimento."
                false
            }
            description.isEmpty() -> {
                binding.fieldDescription.error = "Digite uma descrição para o negócio."
                false
            }
            number.isEmpty() -> {
                binding.fieldNumber.error = "Insira um número de telefone."
                false
            } else -> true
        }
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

    @Throws(IOException::class)
    private fun createImageFile(id: Long, position: Int): File {
        // Create an image file name
        val timeStamp: String = DateFormat.getDateTimeInstance().format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "${id}_${position}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


}
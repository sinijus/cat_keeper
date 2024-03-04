package com.jaanussinivali.catkeeper.ui.cat

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jaanussinivali.catkeeper.data.CatDao
import com.jaanussinivali.catkeeper.data.CatKeeperDatabase
import com.jaanussinivali.catkeeper.data.entity.Cat
import com.jaanussinivali.catkeeper.databinding.DialogEditImageAndNameBinding
import com.jaanussinivali.catkeeper.databinding.FragmentImageCardBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import kotlin.concurrent.thread

const val REQUEST_IMAGE_CAPTURE = 1

class ImageCardFragment : Fragment() {

    private lateinit var binding: FragmentImageCardBinding
    private var parentFragmentNumber: Int = 0
    private val catDao: CatDao by lazy { CatKeeperDatabase.getDatabase(requireContext()).getCatDao() }
    private var capturedImage: Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentImageCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getParentFragmentNumber()
        displayImageCard()
        setCardOnClickListener()
    }

    private fun getParentFragmentNumber() {
        val parentFragment = parentFragment as CatCardsFragment
        parentFragmentNumber = parentFragment.getFragmentNumber()    }

    private fun displayImageCard() {
        val sharedPref = requireContext().getSharedPreferences("${parentFragmentNumber}_image", Context.MODE_PRIVATE)
        val imagePath = sharedPref.getString("image_path", null)
        val imageBitmap = if (imagePath != null) BitmapFactory.decodeFile(imagePath) else null
        binding.imageViewMainPic.setImageBitmap(imageBitmap)
    }

    private fun setCardOnClickListener() {
        binding.cardViewImage.setOnClickListener { showEditDialogNameAndPicture() }
    }

    private fun showEditDialogNameAndPicture() {
        val dialogBinding = DialogEditImageAndNameBinding.inflate(requireActivity().layoutInflater)
        dialogBinding.textInputNameLayout.hint = "Cat name (on tab view)"
        thread {
            val cat = catDao.getCat(parentFragmentNumber)
            requireActivity().runOnUiThread {
                dialogBinding.textInputNameValue.setText(cat.name)
            }
        }

        dialogBinding.buttonInsertCameraImage.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "Image capture error!", Toast.LENGTH_LONG).show()
            }
        }

        dialogBinding.buttonInsertGalleryImage.setOnClickListener {
            Toast.makeText(context, "This feature is in development...", Toast.LENGTH_LONG).show()
        }

        MaterialAlertDialogBuilder(requireContext()).setTitle("Update name and image").setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                thread {
                    catDao.update(
                        Cat(id = parentFragmentNumber, name = dialogBinding.textInputNameValue.text.toString())
                    )
                }
                displayImageCard()
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            capturedImage = imageBitmap
            binding.imageViewMainPic.setImageBitmap(capturedImage)
            val imagePath = saveImageToInternalStorage(capturedImage!!)
            saveImagePathToSharedPreferences(imagePath)
        } else {
            Toast.makeText(context, "Image capture cancelled!", Toast.LENGTH_LONG).show()

        }

    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val wrapper = ContextWrapper(requireContext())
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${parentFragmentNumber}_image.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    private fun saveImagePathToSharedPreferences(imagePath: String) {
        val sharedPref = requireContext().getSharedPreferences("${parentFragmentNumber}_image", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("image_path", imagePath)
        editor.apply()
    }
}
package hu.bme.aut.android.gyakorlas.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentUploadNewPlaceBinding


class UploadNewPlaceFragment : Fragment() {
    private lateinit var binding : FragmentUploadNewPlaceBinding
    var SELECT_PICTURE = 200
    private var imageUri: Uri? = null

    private lateinit var imageContainer: LinearLayout
    private val imageViews = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadNewPlaceBinding.inflate(inflater, container, false)
        imageContainer = binding.llImageViews
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAdd.setOnClickListener(){
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, SELECT_PICTURE)
        }

        binding.btnSave.setOnClickListener(){
            findNavController().navigate(R.id.action_uploadNewPlaceFragment_to_menuFragment)
        }
        binding.btnCancel.setOnClickListener(){
            findNavController().navigate(R.id.action_uploadNewPlaceFragment_to_menuFragment)
        }
        binding.imgbtnMenu.setOnClickListener(){
            findNavController().navigate(R.id.action_uploadNewPlaceFragment_to_menuFragment)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
            //imageUri = data?.data

            data?.data?.let { uri ->
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
                addImageView(Bitmap.createScaledBitmap(bitmap, 450, 450, true))
            }

           // binding.ivPlace.setImageURI(imageUri)
        }
    }

    private fun addImageView(bitmap: Bitmap) {
        val newImageView = ImageView(this.context)
        newImageView.setImageBitmap(bitmap)
        imageContainer.addView(newImageView)
        imageViews.add(newImageView)
    }

}
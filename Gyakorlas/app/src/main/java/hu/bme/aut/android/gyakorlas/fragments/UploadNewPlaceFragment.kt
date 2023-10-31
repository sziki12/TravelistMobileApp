package hu.bme.aut.android.gyakorlas.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.content.pm.PackageManager
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentUploadNewPlaceBinding
import hu.bme.aut.android.gyakorlas.permission.PermissionHandler
import java.security.Permission


class UploadNewPlaceFragment : Fragment() {
    private lateinit var binding : FragmentUploadNewPlaceBinding
    private var popupWindow: PopupWindow? = null
    var SELECT_PICTURE = 200
    var REQUEST_IMAGE_CAPTURE = 300
    private var imageUri: Uri? = null

    private lateinit var imageContainer: LinearLayout
    private val imageViews = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadNewPlaceBinding.inflate(inflater, container, false)
        imageContainer = binding.llImageViews

        binding.fabAdd.setOnClickListener(){
            Log.i("BUTTON", "clicklistener")
            showPopupWindow(binding.fabAdd)
        }
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.fabAdd.setOnClickListener(){
//            this.activity?.let {
//                PermissionHandler.requestPermission(
//                    it,
//                    PermissionHandler.READ_EXTERNAL_STORAGE_REQUEST_CODE,
//                    {
//                        startSelectPicture()
//                        Log.i("PERMISSION", "Access to photos enabled")
//                    })
//                {
//                    Log.i("PERMISSION", "Access to photos disabled")
//                }
//            }
//        }


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

    private fun showPopupWindow(anchorView: View) {
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_window, null)

        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true

        popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow?.showAtLocation(anchorView, Gravity.BOTTOM, 0, 0)

        val uploadPhoto = popupView.findViewById<TextView>(R.id.tvUploadPhoto)
        val takePhoto = popupView.findViewById<TextView>(R.id.tvTakePhoto)


        uploadPhoto.setOnClickListener() {
            this.activity?.let {
                PermissionHandler.requestPermission(
                    it,
                    PermissionHandler.READ_EXTERNAL_STORAGE_REQUEST_CODE,
                    {
                        startSelectPicture()
                        Log.i("PERMISSION", "Access to photos enabled")
                    })
                {
                    Log.i("PERMISSION", "Access to photos disabled")
                }
            }
            popupWindow?.dismiss()
        }

        takePhoto.setOnClickListener(){
            this.activity?.let{
                PermissionHandler.requestPermission(
                    it,
                    PermissionHandler.CAMERA_ACCESS_REQUEST_CODE,
                    {
                        startTakePictureIntent()
                        Log.i("PERMISSION", "Access to camera enabled")
                    })
                {
                    Log.i("PERMISSION", "Access to camera disabled")
                }
            }
            popupWindow?.dismiss()
        }
    }

    private fun startSelectPicture(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, SELECT_PICTURE)
    }

    private fun startTakePictureIntent(){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
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
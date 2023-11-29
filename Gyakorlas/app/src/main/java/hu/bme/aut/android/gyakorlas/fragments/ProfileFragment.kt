package hu.bme.aut.android.gyakorlas.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentProfileBinding
import hu.bme.aut.android.gyakorlas.permission.PermissionHandler

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private var popupWindow: PopupWindow? = null
    var SELECT_PICTURE = 200
    var REQUEST_IMAGE_CAPTURE = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.btnUploadNewPicture.setOnClickListener {
            showPopupWindow(binding.btnUploadNewPicture)
        }

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lastSavedEmailSP = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val lastSavedEmail = lastSavedEmailSP.getString("lastSavedEmail", "")
        Log.i("LASTSAVEDEMAIL", lastSavedEmail.toString())

        val emailUsernameSP = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)

        val username = emailUsernameSP.getString(lastSavedEmail, "")
        Log.i("USERNAME", username.toString())

        binding.etUsername.setText(username)

        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_menuFragment)
        }

        binding.btnSave.setOnClickListener {
            emailUsernameSP.edit {
                putString(lastSavedEmail, binding.etUsername.text.toString())
                apply()
            }

            findNavController().navigate(R.id.action_profileFragment_to_menuFragment)
        }

        binding.imgbtnMenu.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_menuFragment)
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
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURE) {
            data?.data?.let { uri ->
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
                binding.ivProfilePicture.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 450, 450, true))
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.ivProfilePicture.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 450, 450, true))
        }
    }
}
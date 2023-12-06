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
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.model.LatLng
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentUploadNewPlaceBinding
import hu.bme.aut.android.gyakorlas.permission.PermissionHandler
import hu.bme.aut.android.gyakorlas.retrofit.DataAccess
import java.io.ByteArrayOutputStream


class UploadNewPlaceFragment : Fragment() {
    private lateinit var binding : FragmentUploadNewPlaceBinding
    private var popupWindow: PopupWindow? = null
    var SELECT_PICTURE = 200
    var REQUEST_IMAGE_CAPTURE = 300
    private var imageUri: Uri? = null
    private val args : UploadNewPlaceFragmentArgs by navArgs()

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
            showPopupWindow(binding.fabAdd)
        }
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val location = args.location
        if(location!=null)
        {
            binding.etLatitude.setText(location.latitude.toString())
            binding.etLongitude.setText(location.longitude.toString())
        }

        binding.btnSave.setOnClickListener {
            if (binding.etPlaceName.text.toString().isEmpty()){
                binding.etPlaceName.requestFocus()
                binding.etPlaceName.error = "Please enter the name of the place"
            }
            else if (binding.etCityName.text.toString().isEmpty()){
                binding.etCityName.requestFocus()
                binding.etCityName.error = "Please enter the name of the city"
            }
            else if (binding.etLatitude.text.toString().isEmpty()){
                binding.etLatitude.requestFocus()
                binding.etLatitude.error = "Please enter the latitude coordinate"
            }
            else if (binding.etLongitude.text.toString().isEmpty()){
                binding.etLongitude.requestFocus()
                binding.etLongitude.error = "Please enter the longitude coordinate"
            }
            else if (binding.etLatitude.text.toString().toDouble() > 90 || binding.etLatitude.text.toString().toDouble() < -90){
                binding.etLatitude.requestFocus()
                binding.etLatitude.error = "Latitude coordinate must be between -90° and 90°"
            }
            else if (binding.etLongitude.text.toString().toDouble() > 180 || binding.etLongitude.text.toString().toDouble() < -180){
                binding.etLongitude.requestFocus()
                binding.etLongitude.error = "Longitude coordinate must be between -180° and 180°"
            }
            else {
                val name = binding.etPlaceName.text.toString()
                val location = binding.etCityName.text.toString()
                val latitude = binding.etLatitude.text.toString().toDouble()
                val longitude = binding.etLongitude.text.toString().toDouble()
                val description = binding.etDescription.text.toString()
                val rating = binding.simpleRatingBar.rating.toString().toDouble()
                val place = DataAccess.PlaceServerData(name, location, latitude, longitude, description, rating)

                DataAccess.startUploadNewPlaceListener(place, ::onSuccess, ::onFailure)
            }
        }

        binding.btnCancel.setOnClickListener(){
            findNavController().navigate(R.id.action_uploadNewPlaceFragment_to_menuFragment)
        }

        binding.imgbtnMenu.setOnClickListener(){
            findNavController().navigate(R.id.action_uploadNewPlaceFragment_to_menuFragment)
        }

        binding.btnSelectLocation.setOnClickListener()
        {
            var position: LatLng? = null
            if(binding.etLatitude.text.isNotEmpty()&&binding.etLongitude.text.isNotEmpty())
            {
                val lat = binding.etLatitude.text.toString().toDouble()
                val lng = binding.etLongitude.text.toString().toDouble()
                position = LatLng(lat,lng)

            }
            val action = UploadNewPlaceFragmentDirections.actionUploadNewPlaceFragmentToSelectLocationMapFragment(position)
            NavHostFragment.findNavController(this as Fragment).navigate(action)
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
            data?.data?.let { uri ->
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
                addImageView(Bitmap.createScaledBitmap(bitmap, 450, 450, true))
            }
        }

        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            addImageView(Bitmap.createScaledBitmap(imageBitmap, 450, 450, true))
        }
    }

    private fun addImageView(bitmap: Bitmap) {
        val newImageView = ImageView(this.context)
        newImageView.setImageBitmap(bitmap)
        imageContainer.addView(newImageView)
        imageViews.add(newImageView)
    }

    private fun onFailure(message:String)
    {
        Toast.makeText(requireContext(),message, Toast.LENGTH_LONG).show()
        Log.i("Retrofit","OnFailure")
    }
    private fun onSuccess()
    {
        findNavController().navigate(R.id.action_uploadNewPlaceFragment_to_menuFragment)
        Log.i("Retrofit","OnSuccess")
    }

    private fun transormBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        return stream.toByteArray()
    }
}
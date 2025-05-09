package com.example.marketix.presentation.editaccount

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.marketix.BuildConfig
import com.example.marketix.R
import com.example.marketix.databinding.ActivityEditAccountBinding
import com.example.marketix.presentation.login.LoginActivity
import com.example.marketix.presentation.signup.SignupActivity
import com.example.marketix.util.Constants
import com.example.marketix.util.SvgLoader
import com.example.marketix.util.alertMessageDialog
import com.example.marketix.util.checkCameraPermission
import com.example.marketix.util.customToast
import com.example.marketix.util.getPath
import com.example.marketix.util.getPlaceHolder
import com.example.marketix.util.hideKeyboard
import com.example.marketix.util.intentCall
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import java.io.File
import java.util.Objects
import javax.inject.Inject

class EditAccountActivity : DaggerAppCompatActivity(), EditAccountActivityListener {

    private val TAG = SignupActivity::class.java.name
    lateinit var activity: ActivityEditAccountBinding

    private lateinit var currentPhotoPath: String

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: EditAccountViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(EditAccountViewModel::class.java)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_edit_account)
        activity.viewModel = viewModel
        viewModel.listener = this
        activity.lifecycleOwner = this

        viewModel.email.value = ""
        viewModel.address.value = ""
        viewModel.userName.value = ""
        viewModel.phoneNumber.value = ""
        viewModel.loadProfileData()

        viewModel.profileImage.observe(this) {
            it.let {
                if (it.isNotEmpty()) {
                    if (it.endsWith(".svg") || it.endsWith(".SVG"))
                        SvgLoader.loadSvg(this, it, activity.ivProfilePicture)
                    else {
                        Glide.with(this@EditAccountActivity).load(it)
                            .transform(CenterCrop(), RoundedCorners(32))
                            .placeholder(getPlaceHolder())
                            .into(activity.ivProfilePicture)
                    }
                }
            }
        }

    }

    override fun chooseImageClick() {
        try {
            if (checkCameraPermission()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) ActivityCompat.requestPermissions(
                    this@EditAccountActivity, arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    ), Constants.STORAGE_PERMISSION_CHECK
                ) else ActivityCompat.requestPermissions(
                    this@EditAccountActivity, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    ), Constants.STORAGE_PERMISSION_CHECK
                )
            } else {
                chooseImageDialog()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun chooseImageDialog() {
        val dialog = Dialog(this@EditAccountActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_choose_image)
        val close = dialog.findViewById<ImageView>(R.id.ivClose)
        val camera = dialog.findViewById<TextView>(R.id.tvCamera)
        val gallery = dialog.findViewById<TextView>(R.id.tvGallery)
        close.setOnClickListener { dialog.dismiss() }
        camera.setOnClickListener {
            startCamera()
            dialog.dismiss()
        }
        gallery.setOnClickListener {
            galleryLauncher.launch("image/*")
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun startCamera() {
        val imageFile = createTempImageFile()
//        val photoURI: Uri = FileProvider.getUriForFile(
//            this, applicationContext.packageName, imageFile
//        )
        val photoURI: Uri = FileProvider.getUriForFile(
            Objects.requireNonNull(applicationContext),
            BuildConfig.APPLICATION_ID + ".provider", imageFile
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        takePictureLauncher.launch(cameraIntent)
    }

    private fun createTempImageFile(): File {
        val timeStamp: String = System.currentTimeMillis().toString()
        val storageDir: File? = getExternalFilesDir(null)
        return File.createTempFile(
            "JPEG_${timeStamp}_", ".jpg", storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // The photo was captured and saved successfully
                // Display the captured image in your ImageView
//                appCompatImageView.setImageURI(Uri.parse(currentPhotoPath))
                viewModel.profileImage.value = currentPhotoPath
//                    getPath(this@EditAccountActivity, currentPhotoPath).toString()
            } else {
                // Photo capture failed or was canceled
                customToast("Photo capture failed or was canceled")
            }
        }

    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it
        try {
            viewModel.profileImage.value =
                getPath(this@EditAccountActivity, galleryUri!!).toString()
//            binding.image.setImageURI(galleryUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun openUpdateProfileActivity() {
        intent.putExtra("data", 1)
        setResult(Constants.EDIT_PROFILE_DATA, intent)
        finish()

    }

    override fun openBackActivity() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun hideKeyboardListener() {
        hideKeyboard()
    }

    override fun displayMessageListener(message: String) {
        this@EditAccountActivity.alertMessageDialog(message) {
            intentCall<LoginActivity>(2) { }
        }
    }

}
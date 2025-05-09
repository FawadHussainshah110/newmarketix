package com.example.marketix.util

import android.Manifest
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.marketix.R
import com.example.marketix.localehelper.Lingver
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.regex.Pattern


/*
* 0 means short duration (default)
* 1 means long duration
* */
fun Context.customToast(msg: String, duration: Int = 0) {
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(applicationContext, removeBeforeFirstAlphabet(msg), duration).show()
    }
}

fun removeBeforeFirstAlphabet(input: String): String {
    val regex = "[^a-zA-Z]*([a-zA-Z].*)".toRegex()
    return regex.replace(input, "$1")
}

fun Context.getPlaceHolder(): Drawable {
    val drawable = CircularProgressDrawable(this)
    drawable.setColorSchemeColors(
        R.color.colorPrimary,
        R.color.colorPrimaryDark,
        R.color.colorAccent
    )
    drawable.centerRadius = 30f
    drawable.strokeWidth = 5f
    drawable.start()

    return drawable
}

fun Context.customLog(msg: String, tag: String = Constants.DEFAULT_TAG) {
    Log.d(tag, msg)
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun languageCheck(): Boolean {
    return Lingver.getInstance().getLanguage() == Constants.LANGUAGE_ENGLISH
}

fun Activity.startCustomAnimation(animation: Int): Animation {
    val anim: Animation =
        AnimationUtils.loadAnimation(this, animation)
    anim.interpolator = LinearInterpolator() // for smooth animation

    return anim
}

fun Activity.checkCameraPermission(): Boolean {
    return ((ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) != PackageManager.PERMISSION_GRANTED)
            || (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) != PackageManager.PERMISSION_GRANTED))
}

fun Activity.checkContactsPermission(): Boolean {
    return ((ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_CONTACTS
    ) != PackageManager.PERMISSION_GRANTED)
            || (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_CONTACTS
    ) != PackageManager.PERMISSION_GRANTED))
}

fun Context.checkPermissionFunction(): Boolean {
    return if (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        true
    } else {
        this.requestPermissions()
        false
    }
}

fun Activity.isKeyboardVisible(): Boolean {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.isAcceptingText

}

fun Context.requestPermissions() {
    ActivityCompat.requestPermissions(
        this as Activity,
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ),
        1
    )
}

fun getImageUri(
    context: Context,
    photo: Bitmap,
    folderName: String = context.resources.getString(R.string.app_name),
    fileName: String = "temp"
): Uri? {

    val fos: OutputStream
    var imageFile: File? = null
    var imageUri: Uri? = null

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver: ContentResolver = context.contentResolver
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        contentValues.put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            Environment.DIRECTORY_DCIM + File.separator + folderName
        )
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        fos = resolver.openOutputStream(imageUri!!)!!
    } else {
        val imagesDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DCIM
        ).toString() + File.separator + folderName
        imageFile = File(imagesDir)
        if (!imageFile.exists()) {
            imageFile.mkdir()
        }
        imageFile = File(imagesDir, "$fileName.png")
        fos = FileOutputStream(imageFile)
    }

    val saved: Boolean = photo.compress(Bitmap.CompressFormat.PNG, 100, fos)
    Log.d("1234_extension", "$saved")
    fos.flush()
    fos.close()

    if (imageFile != null) { // pre Q
        MediaScannerConnection.scanFile(context, arrayOf(imageFile.toString()), null, null)
        imageUri = Uri.fromFile(imageFile)
    }

    return imageUri

}

fun showImageUsingUri(context: Context, img: ImageView, uri: Uri) {
    val stream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(stream).fixRotation(context, uri)
    img.setImageBitmap(bitmap)
}

fun Bitmap.fixRotation(context: Context, uri: Uri): Bitmap? {

    val ei = getPath(context, uri)?.let { ExifInterface(it) }

    val orientation: Int = ei?.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_UNDEFINED
    ) ?: ExifInterface.ORIENTATION_NORMAL

    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(270f)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

fun Bitmap.rotateImage(angle: Float): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        this, 0, 0, width, height,
        matrix, true
    )
}

/*
finishCheck values explanation
    0-> default value
    1-> finish current activity
    2 or more -> finishAffinity
ActivityForResult values explanation
    0-> default value
    1-> startActivityForResult
*/
inline fun <reified T : Activity> Activity.intentCall(
    finishCheck: Int = 0, forResultCode: Int = 0,
    noinline extras: Bundle.() -> Unit = {}
) {

    val intent = Intent(this, T::class.java)
    if (extras != null)
        intent.putExtras(Bundle().apply(extras))

    if (forResultCode == 0)
        startActivity(intent)
    else
        startActivityForResult(intent, forResultCode)

    if (finishCheck == 1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            finish()
        }
    } else if (finishCheck > 1) {
        finishAffinity()
    }
}

inline fun <reified T : Activity> Activity.intentCallForResult(
    result: ActivityResultLauncher<Intent>,
    noinline extras: Bundle.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java)
    if (extras != null)
        intent.putExtras(Bundle().apply(extras))
    result.launch(intent)
}

fun String.isMobileValid(): Boolean {
    // 11 digit number start with 011 or 010 or 015 or 012
    // then [0-9]{8} any numbers from 0 to 9 with length 8 numbers
    if (Pattern.matches("\\([0-9]{1}[0-9]{2}\\) [0-9]{3}\\-[0-9]{4}$", this)) {
        return true
    }
    return false
}

fun Context.formatedPhoneNumber(string: String): String {

    if (string.startsWith("+966")) {
        val firstPart = string.substring(0, 4)
        val secondPart = string.substring(4, string.length)
        return "$firstPart $secondPart"
    }
    return string
}

fun Context.removeSpacesFromPhoneNumber(string: String): String {
    return string.replace(" ", "")
}

fun extractOnlyPhoneNumber(string: String): String {
    val temp = string.filter { it in '0'..'9' }
    return "+${temp}"
}

fun Context.removeSpaceFromString(number: String): String {
    return number.replace(" ", "")
}

fun hideCreditCardNumber(string: String): String {
    var check = 0
    if (string.length > 10)
        check = string.length - 4

    val newString = string.substring(check, string.length)
    var temp = ""
    for (i in 0 until check) {
        temp += "*"
    }

    return "$temp$newString"
}

fun convertNumberToCreditCardFormat(string: String): String {
    return string.chunked(4).joinToString(separator = " ")
}

fun convertNumberToCreditCardDateFormat(string: String): String {
    return string.chunked(2).joinToString(separator = "/")
}

fun removeCreditCardSeparator(s: String): String {
    return s.replace(" ", "")
}

fun removeCreditCardDateSeparator(s: String): String {
    return s.replace("/", "")
}

fun String.isEmailValid() =
    !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isEmpty() =
    TextUtils.isEmpty(this)

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.cancelTransition() {
    transitionName = null
}

fun View.isVisible() = this.visibility == View.VISIBLE

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.GONE
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)


fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}

fun Activity.showKeyboard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view: View? = this.currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View?.fitSystemWindowsAndAdjustResize() = this?.let { view ->
    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
        view.fitsSystemWindows = true
        val bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom

        WindowInsetsCompat
            .Builder()
            .setInsets(
                WindowInsetsCompat.Type.systemBars(),
                Insets.of(0, 0, 0, bottom)
            )
            .build()
            .apply {
                ViewCompat.onApplyWindowInsets(v, this)
            }
    }
}

fun isInternetAvailable(context: Context): Boolean {
    if (context == null) return false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return isInternet()
                }
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return true
        }
    }
    return false
}

private fun isInternet(): Boolean {
    val runtime = Runtime.getRuntime()
    try {
        val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
        val exitValue = ipProcess.waitFor()
        Log.d("network_connection", exitValue.toString() + "")
        return exitValue == 0
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
    return false
}

inline fun <reified T> Context.parseStingToObjetModel(string: String): T {
    return Gson().fromJson(string, T::class.java)
}
//
//fun Context.showResponseErrors(model: CommonResponse) {
//
//    when {
//        model.errorCode == 7002 -> {
//            customToast(model.error)
//        }
//
//        !model.errorMessages.isNullOrEmpty() -> {
//            customToast(model.errorMessages[0])
//        }
//
//        model.error.isNotEmpty() -> {
//            customToast(model.error)
//        }
//
//        else -> {
//            customToast(resources.getString(R.string.invalid_input_401))
//        }
//    }
//}

fun Context.showHTTPExceptionErrors(model: HttpException) {

    if (model.code() == 401) {
        customToast(resources.getString(R.string.invalid_input_401))
    } else
        customToast("Unknown error")

}

@RequiresApi(Build.VERSION_CODES.KITKAT)
fun getPath(context: Context, uri: Uri): String? {
    val isKitKatorAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    // DocumentProvider
    if (isKitKatorAbove && DocumentsContract.isDocumentUri(context, uri)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }

        } else if (isDownloadsDocument(uri)) {
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(
                    id
                )
            )
            return getDataColumn(context, contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])
            return getDataColumn(context, contentUri, selection, selectionArgs)
        }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
//        return uri.path
        return getDataColumn(context, uri, null, null)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }
    return null
}

fun getDataColumn(
    context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)
    try {
        cursor = context.contentResolver.query(
            uri!!, projection, selection, selectionArgs, null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val column_index: Int = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(column_index)
        }

//        val returnCursor: Cursor? = context.contentResolver.query(uri!!, null, null, null, null)
//        val columnIndex = returnCursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        returnCursor?.moveToFirst();
//        val path = returnCursor?.getString(columnIndex!!)
//        return path
    } finally {
        if (cursor != null) cursor.close()
    }
    return null
}

fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

fun Long.convertMilliSecToTime(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return formatter.format(calendar.time)
}

fun String.reformatDate(isTimeZone: Boolean = false): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    if (isTimeZone)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
    val convertedDate: Date?
    var formattedDate = ""
    try {
        convertedDate = sdf.parse(this)
        formattedDate = SimpleDateFormat("MMM dd, hh:mm aa").format(convertedDate)
    } catch (e: ParseException) {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        if (isTimeZone)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
        val convertedDate: Date?
        try {
            convertedDate = sdf.parse(this)
            formattedDate = SimpleDateFormat("MMM. dd").format(convertedDate)
        } catch (e: ParseException) {
            return this
        }
    }
    return formattedDate
}

fun String.reformatDateProfile(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    // sdf.timeZone = TimeZone.getTimeZone("GMT-5")
    val convertedDate: Date?
    var formattedDate = ""
    try {
        convertedDate = sdf.parse(this)
        formattedDate = SimpleDateFormat("MM-dd-yyyy").format(convertedDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return formattedDate
}

fun String.reformatDateProfileApiFormat(): String {
    val sdf = SimpleDateFormat("MM-dd-yyyy")
    // sdf.timeZone = TimeZone.getTimeZone("UTC")
    val convertedDate: Date?
    var formattedDate = ""
    try {
        convertedDate = sdf.parse(this)
        formattedDate = SimpleDateFormat("yyyy-MM-dd").format(convertedDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return formattedDate
}


fun String.reformatDateAndAddTime(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    sdf.timeZone = TimeZone.getTimeZone("UTC")

    val convertedDate: Date?
    var formattedDate = ""
    try {
        convertedDate = sdf.parse(this)
        val newDate = Date(convertedDate.time)
        formattedDate = SimpleDateFormat("MMM dd, hh:mm aa").format(newDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return formattedDate
}

fun String.weekDayFromDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
//    sdf.timeZone = TimeZone.getTimeZone("UTC")

    val convertedDate: Date?
    var formattedDate = ""
    try {
        convertedDate = sdf.parse(this)
        val newDate = Date(convertedDate.time)
        formattedDate = SimpleDateFormat("EEE MM-dd-yyyy").format(newDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return formattedDate
}

fun String.getTimeAgo(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val pastTime: Date = dateFormat.parse(this)
    val currentTime: Date = calendar.time

    val diff = currentTime.time - pastTime.time

    val SECOND_MILLIS = 1000
    val MINUTE_MILLIS = 60 * SECOND_MILLIS
    val HOUR_MILLIS = 60 * MINUTE_MILLIS
    val DAY_MILLIS = 24 * HOUR_MILLIS
    val WEEK_MILLIS = 7 * DAY_MILLIS

    calendar.time = pastTime

    return if ((diff.toLong() < SECOND_MILLIS)) {
        "a second ago"
    } else if ((diff.toLong() < 2 * MINUTE_MILLIS)) {
        "a minute ago"
    } else if (diff.toLong() / MINUTE_MILLIS in 1..58) {
        "${diff.toLong() / MINUTE_MILLIS} minutes ago"
    } else if (diff.toLong() < 2 * HOUR_MILLIS) {
        "an hour ago"
    } else if (diff.toLong() / HOUR_MILLIS in 1..23) {
        "${diff.toLong() / HOUR_MILLIS} hours ago"
    } else if (diff.toLong() < 2 * DAY_MILLIS) {
        "yesterday"
    } else if ((diff.toLong() / DAY_MILLIS in 1..6)) {
        "${diff.toLong() / DAY_MILLIS} days ago"
    } else if ((diff.toLong() < 2 * WEEK_MILLIS)) {
        "a week ago"
    } else if ((diff.toLong() / WEEK_MILLIS > 0)) {
        "${diff.toLong() / WEEK_MILLIS} weeks ago"
    } else {
        "a months ago"
    }

}

fun String.getTimeAgoForNotifications(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val pastTime: Date = dateFormat.parse(this)
    val currentTime: Date = calendar.time

    val diff = currentTime.time - pastTime.time

    val SECOND_MILLIS = 1000
    val MINUTE_MILLIS = 60 * SECOND_MILLIS
    val HOUR_MILLIS = 60 * MINUTE_MILLIS
    val DAY_MILLIS = 24 * HOUR_MILLIS
    val WEEK_MILLIS = 7 * DAY_MILLIS

    calendar.time = pastTime

    return if ((diff.toLong() < SECOND_MILLIS)) {
        "1m"
    } else if ((diff.toLong() < 2 * MINUTE_MILLIS)) {
        "1m"
    } else if (diff.toLong() / MINUTE_MILLIS in 1..58) {
        "${diff.toLong() / MINUTE_MILLIS}m"
    } else if (diff.toLong() < 2 * HOUR_MILLIS) {
        "1h"
    } else if (diff.toLong() / HOUR_MILLIS in 1..23) {
        "${diff.toLong() / HOUR_MILLIS}h"
    } else if (diff.toLong() < 2 * DAY_MILLIS) {
        "1d"
    } else if ((diff.toLong() / DAY_MILLIS in 1..6)) {
        "${diff.toLong() / DAY_MILLIS}d"
    } else if ((diff.toLong() < 2 * WEEK_MILLIS)) {
        "1w"
    } else if ((diff.toLong() / WEEK_MILLIS > 0)) {
        "${diff.toLong() / WEEK_MILLIS}w"
    } else {
        "1m"
    }

}


fun String.dateDifference(secondDate: String): Long {
    val dateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
//    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val firstDate: Date = dateFormat.parse(this)
    val secDate: Date = dateFormat.parse(secondDate)

    return (secDate.time - firstDate.time).toLong()
}

fun String.timeDifference(secondTime: String): Int {
    val dateFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
    val firstTime: Date = dateFormat.parse(this)
    val secTime: Date = dateFormat.parse(secondTime)

    return (secTime.time - firstTime.time).toInt()
}

fun convertTo12Hours(militaryTime: String): String {
    //in => "14:00:00"
    //out => "02:00 PM"
    val inputFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("hh:mm aa", Locale.getDefault())
    val date = inputFormat.parse(militaryTime)
    return outputFormat.format(date)
}

fun String.copyTextToClipboard(context: Activity) {
    val myClipboard = getSystemService(context, ClipboardManager::class.java) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText("Address", this)
    myClipboard.setPrimaryClip(clip)
}

fun Activity.showSharingDialogAsKotlin(text: String) {
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, text)
    this.startActivity(Intent.createChooser(intent, "Share with:"))
}

fun Context.showSharingDialogAsKotlin(text: String) {
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, text)
    this.startActivity(Intent.createChooser(intent, "Share with:"))
}

fun Context.getAppName(): String {
    var appName: String = ""
    val applicationInfo = applicationInfo
    val stringId = applicationInfo.labelRes
    appName = if (stringId == 0) {
        applicationInfo.nonLocalizedLabel.toString()
    } else {
        getString(stringId)
    }
    return appName
}

fun String.splitString(): List<String> {
    return this.split("_")
}

fun String.splitDate(): List<String> {
    return this.split("-")
}

fun String.validUrl(): String {
    var tempString = this
    if (!tempString.startsWith("http"))
        tempString = "https://$tempString"

    return tempString
}

fun Int.toPx(): Float = this * Resources.getSystem().displayMetrics.density

fun View.dpTtoPx(value: Float): Float = value * context.resources.displayMetrics.density

fun View.animateTranslationX(
    value: Float,
    animationDuration: Long = 400,
    animationInterpolator: Interpolator = DecelerateInterpolator()
): ObjectAnimator? {
    val view = this@animateTranslationX
    if (view.translationX == value) return null

    return ObjectAnimator.ofFloat(view, "translationX", value).apply {
        duration = animationDuration
        interpolator = animationInterpolator
        start()
    }
}

fun RecyclerView.isLastVisible(): Boolean {
    val layoutManager: LinearLayoutManager = this.layoutManager as LinearLayoutManager
    val pos: Int = layoutManager.findLastCompletelyVisibleItemPosition()
    val numItems: Int = this.adapter?.itemCount ?: 0
    return pos >= numItems - 3
}

fun setBadgeSony(context: Context, count: Int) {
    val launcherClassName: String = getLauncherClassName(context) ?: return
    val intent = Intent()
    intent.action = "com.sonyericsson.home.action.UPDATE_BADGE";
    intent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", launcherClassName)
    intent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", true)
    intent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", count)
    intent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.packageName)
    context.sendBroadcast(intent)
}

fun setBadgeSamsung(context: Context, count: Int) {
    val launcherClassName: String = getLauncherClassName(context) ?: return
    val intent = Intent("android.intent.action.BADGE_COUNT_UPDATE")
    intent.putExtra("badge_count", count)
    intent.putExtra("badge_count_package_name", context.packageName)
    intent.putExtra("badge_count_class_name", launcherClassName)
    context.sendBroadcast(intent)
}

fun setBadgeOthers(context: Context, count: Int) {
    val launcherClassName: String = getLauncherClassName(context) ?: return
    val intent = Intent("android.intent.action.BADGE_COUNT_UPDATE")
    intent.putExtra("badge_count", count)
    intent.putExtra("badge_count_package_name", context.packageName)
    intent.putExtra("badge_count_class_name", launcherClassName)
    context.sendBroadcast(intent)
}


fun getLauncherClassName(context: Context): String? {
    val pm = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    val resolveInfos: List<ResolveInfo> = pm.queryIntentActivities(intent, 0)
    for (resolveInfo in resolveInfos) {
        val pkgName: String = resolveInfo.activityInfo.applicationInfo.packageName
        if (pkgName.equals(context.packageName, ignoreCase = true)) {
            return resolveInfo.activityInfo.name
        }
    }
    return null
}

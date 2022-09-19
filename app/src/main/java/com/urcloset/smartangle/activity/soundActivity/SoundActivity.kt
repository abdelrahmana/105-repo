package com.urcloset.smartangle.activity.soundActivity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.chibde.visualizer.*
import com.facebook.shimmer.ShimmerFrameLayout
import com.tyorikan.voicerecordingvisualizer.RecordingSampler
import com.tyorikan.voicerecordingvisualizer.VisualizerView
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.R
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.CategoryModel
import com.urcloset.smartangle.model.UserProfileModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sound.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap


class SoundActivity : TemplateActivity(), RecordingSampler.CalculateVolumeListener {
    var disposable = CompositeDisposable()
    private val LOG_TAG = "AudioRecordTest"
    private val REQUEST_RECORD_AUDIO_PERMISSION = 6546
    var mStartRecording = true
    var mStartPlaying = true
    var tempname = ""
    private var fileName: String = ""
    private var myaccount: UserProfileModel.Data.User? = null
    private var recordButton: ImageView? = null
    private var recorder: MediaRecorder? = null
    private var startVisulaize: Boolean = false
    private var playButton: Button? = null
    private var btnSend: CardView? = null
    private var player: MediaPlayer? = null
    private var barVisualizer: BarVisualizer? = null
    private var permissionToRecordAccepted = false
    private var mRecordingSampler: RecordingSampler? = null
    private var mVisualizerView: VisualizerView? = null
    lateinit var shimmerRemove: ShimmerFrameLayout
    lateinit var shimmerAdd: ShimmerFrameLayout
    lateinit var remove: CardView
    lateinit var progressBar: ProgressBar
    var is_recorded: Boolean = false
    var is_recording: Boolean = false
    lateinit var btnUpload: CardView
    val RESULT_UPLOAD_AUDIO = 2345
    var audioFile: Uri? = null
    var isUploadingFromDeviceFile = false


    private var permissions: Array<String> = arrayOf(
        Manifest.permission.RECORD_AUDIO
    )
    private var writepermissions: Array<String> = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun set_layout() {
        setContentView(R.layout.activity_sound)

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(REQUEST_RECORD_AUDIO_PERMISSION==requestCode) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                permissionToRecordAccepted=true
                onRecord(mStartRecording)
                mStartRecording = !mStartRecording
                mRecordingSampler = RecordingSampler()
                mRecordingSampler?.setVolumeListener(this)
                mRecordingSampler?.setSamplingInterval(100)
                mRecordingSampler?.link(mVisualizerView)
                if (mRecordingSampler!!.isRecording()) {
                    mRecordingSampler!!.stopRecording();
                } else {



                    mRecordingSampler!!.startRecording();
                }
            }

            else{
                permissionToRecordAccepted=false
            }
        }
        if (requestCode == 1111) {
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED){
                RQ(audioFile!!)
            }
        }
        else {
        }
    }


    private fun onRecord(start: Boolean) = if (start) {
        recordButton?.setImageResource(R.drawable.stop_record_voice)
        startRecording()
    } else {
        recordButton?.setImageResource(R.drawable.ic_record_voice)

        stopRecording()
    }

    private fun onPlay(start: Boolean) = if (start) {
        startPlaying()
        barVisualization(player)
    } else {
        stopPlaying()
    }


    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
                if (!startVisulaize) {
                    startVisulaize = true

                }
            } catch (e: IOException) {

                Log.e(LOG_TAG, "prepare() failed  startPlaying")
                Log.e(LOG_TAG, e.localizedMessage)
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        Log.i(LOG_TAG, fileName)
        player = null
    }

    private fun startRecording() {

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                Log.i(LOG_TAG, "success")
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed startRecording")
            }
            is_recorded = true
            is_recording = true
            start()


        }


    }


    private fun stopRecording() {
        if(null !=recorder) {
            try {

                recorder!!.stop()
                recorder!!.release()

                is_recording = false
                recorder = null
            }
            catch (e:Exception){

            }
        }
    }

    override fun init_activity(savedInstanceState: Bundle?) {


    }

    override fun onStop() {
        super.onStop()
        recorder?.release()
        recorder = null
        player?.release()
        player = null
        btnUpload = btn_upload
    }

    override fun init_views() {

        recordButton = findViewById(R.id.btn_record)
        playButton = findViewById(R.id.btn_stop)
        btnSend = findViewById(R.id.btn_send)
        barVisualizer = findViewById<BarVisualizer>(R.id.visualizerBar)
        mVisualizerView =
            findViewById<com.tyorikan.voicerecordingvisualizer.VisualizerView>(R.id.visualizer)
     //   shimmerRemove = findViewById(R.id.shimmer_remove)
        remove = findViewById(R.id.btn_remove_sound)
     //   shimmerAdd = findViewById(R.id.shimmer_add)
        progressBar = findViewById(R.id.progress)
        btnUpload = findViewById(R.id.btn_upload)

        fileName =
            "${externalCacheDir!!.absolutePath}/audiorecordtest${Calendar.getInstance().timeInMillis}.3gp"
        tempname = "audiorecordtest" + (Calendar.getInstance().timeInMillis).toString()
        fileName = "${externalCacheDir!!.absolutePath}/${tempname}.3gp"

        //ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

    }

    override fun init_events() {


        remove.setOnClickListener {
            Log.i("TEST_TEST","TEST")

            if (!isUploadingFromDeviceFile) {
                if (myaccount?.voicIdentity != null) {
                    if (myaccount?.voicIdentity?.mediaPath != null) {
                        if (myaccount?.voicIdentity?.mediaPath!!.isNotEmpty()) {
                            deleteVoiceIdentity(myaccount?.voicIdentity?.mediaPath!!)

                        } else {
                            showToastMessage(getString(R.string.no_voice_identifiy))


                        }
                    } else {
                        showToastMessage(getString(R.string.no_voice_identifiy))
                    }
                } else {
                    showToastMessage(getString(R.string.no_voice_identifiy))


                }

            }else {
                Log.i("TEST_TEST","TEST")
            }
        }




        btnUpload.setOnClickListener {
            if (!isUploadingFromDeviceFile) {

                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "audio/*"
                startActivityForResult(intent, RESULT_UPLOAD_AUDIO)
            }

        }
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }

        btnSend!!.setOnClickListener {
            if (!isUploadingFromDeviceFile) {
                if (is_recorded || audioFile != null)
                    if (is_recording == false || audioFile != null)
                        RQ()
                    else showToastMessage(R.string.no_voice_identifiy)
                else {
                    showToastMessage(R.string.no_voice_identifiy)
                }
            }
        }


        val observer = mVisualizerView!!.viewTreeObserver
        observer.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mVisualizerView!!.setBaseY(mVisualizerView!!.height)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mVisualizerView!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    mVisualizerView!!.viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
            }
        })




        mRecordingSampler = RecordingSampler()
        mRecordingSampler?.setVolumeListener(this)
        mRecordingSampler?.setSamplingInterval(100)
        mRecordingSampler?.link(mVisualizerView)


        recordButton!!.setOnClickListener {
            if (!isUploadingFromDeviceFile) {



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                        Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.i("TEST_TEST", "FRIST")
                    requestPermissions(
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        REQUEST_RECORD_AUDIO_PERMISSION
                    )
                    //callback onRequestPermissionsResult
                } else {


                    onRecord(mStartRecording)
                    mStartRecording = !mStartRecording
                    if (mRecordingSampler!!.isRecording()) {
                        mRecordingSampler!!.stopRecording();
                    } else {
                        mRecordingSampler!!.startRecording();
                    }
                }
            }

            playButton!!.setOnClickListener {
                if (!isUploadingFromDeviceFile) {
                    onPlay(mStartPlaying)
                    mStartPlaying = !mStartPlaying

                }
            }



        }



        getUserProfile()
    }

    override fun set_fragment_place() {

    }

    fun getUserProfile() {
        progressBar.visibility = View.VISIBLE
        remove.visibility=View.GONE
        btnUpload.visibility=View.GONE
        btnSend?.visibility=View.GONE
        val shopApi =
            ApiClient.getClientJwt(
                TemplateActivity.loginResponse?.data?.accessToken!!,
                BasicTools.getProtocol(
                    applicationContext
                ).toString(),
                "en"
            )
                ?.create(
                    AppApi::class.java
                )
        val observable = shopApi!!.getUserProfile(loginResponse?.data?.user?.id!!)

        disposable.clear()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<UserProfileModel>(this) {
                    override fun onSuccess(result: UserProfileModel) {
                        progressBar.visibility = View.GONE


                        if (result.status!!) {




                            btnUpload.visibility = View.VISIBLE
                            myaccount = result.data!!.user








                            if (!result.data.user?.voicIdentity?.mediaPath.isNullOrEmpty()) {
                                findViewById<TextView>(R.id.tv_edit).text =
                                    resources.getText(R.string.edit)
                                btnSend?.visibility = View.VISIBLE
                                remove.visibility = View.VISIBLE
                            } else {
                                findViewById<TextView>(R.id.tv_edit).text =
                                    resources.getText(R.string.add)
                                btnSend?.visibility = View.VISIBLE
                                remove.visibility = View.GONE

                            }


                            if (myaccount?.voicIdentity != null) {
                                if (myaccount?.voicIdentity?.mediaPath != null) {
                                    if (myaccount?.voicIdentity?.mediaPath!!.isNotEmpty()) {
                                        remove.visibility = View.VISIBLE
                                        btnSend?.visibility = View.VISIBLE
                                        findViewById<TextView>(R.id.tv_edit).text = resources.getText(R.string.edit)


                                    } else {
                                        findViewById<TextView>(R.id.tv_edit).text = resources.getText(R.string.add)

                                    }


                                } else {
                                    findViewById<TextView>(R.id.tv_edit).text = resources.getText(R.string.add)

                                }
                            } else {
                                findViewById<TextView>(R.id.tv_edit).text = resources.getText(R.string.add)

                            }



                        }


                    }

                    override fun onFailed(status: Int) {
                        progressBar.visibility = View.GONE


                    }
                })
        )

    }

    fun getProfile() {
        val shopApi =
            ApiClient.getClientJwt(
                TemplateActivity.loginResponse?.data?.accessToken!!,
                BasicTools.getProtocol(
                    applicationContext
                ).toString(),
                "en"
            )
                ?.create(
                    AppApi::class.java
                )
        val observable = shopApi!!.getUserProfile(loginResponse?.data?.user?.id!!)

        disposable.clear()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<UserProfileModel>(this) {
                    override fun onSuccess(result: UserProfileModel) {


                        if (result.status!!) {
                            myaccount = result.data!!.user
                            if (!result.data.user?.voicIdentity?.mediaPath.isNullOrEmpty()) {
//                                findViewById<TextView>(R.id.tv_edit).text =
//                                    resources.getText(R.string.add)
//                                btnSend?.visibility = View.VISIBLE
                            }


                        }


                    }

                    override fun onFailed(status: Int) {


                    }
                })
        )

    }


    private fun getFile(uri: Uri): File? {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r", null)

        parcelFileDescriptor?.let {
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(cacheDir, tempname)
            val outputStream = FileOutputStream(file)

            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            FileUtils.copy(inputStream,outputStream)
        }*/
            return file
        }
        return null
    }

    private fun getAudioFile(uri: Uri): File? {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r", null)

        parcelFileDescriptor?.let {
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(cacheDir, tempname)
            val outputStream = FileOutputStream(file)

            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            FileUtils.copy(inputStream,outputStream)
        }*/
            return file
        }
        return null
    }


    fun getFileName(fileUri: Uri): String {

        var name = ""
        val returnCursor = contentResolver.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }

        return name
    }

    fun RQ() {
        progressBar.visibility = View.VISIBLE
        remove.visibility=View.GONE
        btnUpload.visibility=View.GONE
        btnSend?.visibility=View.GONE
        var body: MultipartBody.Part? = null
        if (BasicTools.isConnected(this@SoundActivity)) {

            btnSend?.visibility = View.GONE
          //  shimmerAdd.visible()
            //  val file = getFile(Uri.fromFile( File(fileName)))!!
            //val file =    Uri.fromFile( File(fileName))
            val file = File(fileName)
            val requestFile = RequestBody.Companion.create(
                "multipart/form-data".toMediaTypeOrNull(),
                file
            )
            body = MultipartBody.Part.createFormData(
                "record_file",
                file.getName(),
                requestFile
            )







            Log.i(LOG_TAG, "begin")

            val shopApi = ApiClient.getClientJwt(
                BasicTools.getToken(this@SoundActivity),
                BasicTools.getProtocol(this@SoundActivity).toString()
            )
                ?.create(AppApi::class.java)

            val observable = shopApi!!.sendSound(body)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ResponseBody>(this@SoundActivity) {
                        override fun onSuccess(result: ResponseBody) {
                           // shimmerAdd.hide()
                           // shimmerAdd.visibility = View.GONE

                            progressBar.visibility = View.GONE
                            remove.visibility=View.VISIBLE

                            findViewById<TextView>(R.id.tv_edit).text =
                                resources.getText(R.string.edit)

                            btnSend?.visibility = View.VISIBLE

                            remove.visibility = View.VISIBLE
                            // getProfile()

                            BasicTools.exitActivity(this@SoundActivity)



                            Log.i(LOG_TAG, "success")
                            showToastMessage(R.string.success)


                        }

                        override fun onFailed(status: Int) {
                          //  shimmerAdd.hide()
                            btnSend?.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE

                            Log.i(LOG_TAG, "faild")
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }

                        override fun onError(e: Throwable) {
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                applicationContext,
                                "" + e.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            super.onError(e)
                        }
                    })
            )

        } else {

            showToastMessage(R.string.no_connection)
        }
    }


    fun RQ(uri: Uri) {

        progressBar.visibility = View.VISIBLE
        remove.visibility=View.GONE
        btnUpload.visibility=View.GONE
        btnSend?.visibility=View.GONE
        val file = getFile(uri)!!
        var body: MultipartBody.Part? = null
        if (BasicTools.isConnected(this@SoundActivity)) {
            isUploadingFromDeviceFile = true
            btnSend?.visibility = View.GONE
            //shimmerAdd.visible()
            //  val file = getFile(Uri.fromFile( File(fileName)))!!
            //val file =    Uri.fromFile( File(fileName))
            // val file = File(fileName)
            val requestFile = RequestBody.Companion.create(
                "multipart/form-data".toMediaTypeOrNull(),
                file
            )
            body = MultipartBody.Part.createFormData(
                "record_file",
                getFileName(uri),
                requestFile
            )







            Log.i(LOG_TAG, "begin")

            val shopApi = ApiClient.getClientJwt(
                BasicTools.getToken(this@SoundActivity),
                BasicTools.getProtocol(this@SoundActivity).toString()
            )
                ?.create(AppApi::class.java)

            val observable = shopApi!!.sendSound(body)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ResponseBody>(this@SoundActivity) {
                        override fun onSuccess(result: ResponseBody) {
                            isUploadingFromDeviceFile = false

                            progressBar.visibility = View.GONE
                           // remove.visibility=View.GONE
                          //  btnUpload.visibility=View.GONE
                          //  btnSend.visibility=View.GONE

                           // shimmerAdd.hide()
                           // shimmerAdd.visibility = View.GONE
                            findViewById<TextView>(R.id.tv_edit).text =
                                resources.getText(R.string.edit)

                            btnSend?.visibility = View.VISIBLE

                            remove.visibility = View.VISIBLE
                            // getProfile()

                            BasicTools.exitActivity(this@SoundActivity)



                            Log.i(LOG_TAG, "success")
                            showToastMessage(R.string.success)


                        }

                        override fun onFailed(status: Int) {
                            progressBar.visibility = View.GONE

                         //   shimmerAdd.hide()
                            btnSend?.visibility = View.VISIBLE

                            Log.i(LOG_TAG, "faild")
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }

                        override fun onError(e: Throwable) {
                            isUploadingFromDeviceFile = false

                            progressBar.visibility = View.GONE


                            Toast.makeText(
                                applicationContext,
                                "" + e.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            super.onError(e)
                        }
                    })
            )

        } else {

            showToastMessage(R.string.no_connection)
        }
    }

    fun barVisualization(player: MediaPlayer?) {
        barVisualizer?.visibility = View.VISIBLE
        // set the custom color to the line.
        barVisualizer?.setColor(ContextCompat.getColor(this, R.color.white))

        // define a custom number of bars we want in the visualizer it is between (10 - 256).
        barVisualizer?.setDensity(80f)

        // Set your media player to the visualizer.
        if (player != null)
            barVisualizer?.setPlayer(player!!.getAudioSessionId())
    }


    override fun onCalculateVolume(volume: Int) {
    }

    fun deleteVoiceIdentity(file: String) {

        progressBar.visibility = View.VISIBLE
        remove.visibility=View.GONE
        btnUpload.visibility=View.GONE
        btnSend?.visibility=View.GONE
     //   shimmerRemove.visible()
      //  remove.visibility = View.GONE


        if (BasicTools.isConnected(this)) {
            val shopApi =
                ApiClient.getClientJwt(
                    TemplateActivity.loginResponse?.data?.accessToken!!,
                    BasicTools.getProtocol(
                        applicationContext
                    ).toString(),
                    "en"
                )
                    ?.create(
                        AppApi::class.java
                    )
            val map = HashMap<String, String>()
            map.put("record_file", file)
            val observable = shopApi!!.deleteVoiceIdentity(map, "ar")

            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<UserProfileModel>(this) {
                        override fun onSuccess(result: UserProfileModel) {
                            progressBar.visibility = View.GONE

                         //   shimmerRemove.hide()
                          //  shimmerRemove.visibility = View.GONE
                           remove.visibility = View.GONE
                            btnUpload.visibility=View.VISIBLE
                            btnSend?.visibility = View.VISIBLE
                            findViewById<TextView>(R.id.tv_edit).text =
                                resources.getText(R.string.add)



                            if (result.status!!) {
                                Toast.makeText(
                                    applicationContext,
                                    result.messages,
                                    Toast.LENGTH_SHORT
                                ).show()


                            }


                        }

                        override fun onFailed(status: Int) {
                            progressBar.visibility = View.GONE
                            btnUpload.visibility=View.VISIBLE
                          //  shimmerRemove.hide()
                            remove.visibility = View.VISIBLE


                            showToastMessage(R.string.faild)


                        }
                    })
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_UPLOAD_AUDIO && resultCode == RESULT_OK && null != data) {


            if (data.data != null) {
                audioFile = data.data
                Log.i("TEST_TEST", audioFile.toString())
                RQ(data.data!!)


            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    111
                )
            }


        } else {
            showToastMessage(R.string.faild)
        }


        super.onActivityResult(requestCode, resultCode, data)
    }


    fun checkWritePremission(): Boolean {

        if (Build.VERSION.SDK_INT >= 23) {
            //do your check here
            return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else {
            ActivityCompat.requestPermissions(this, writepermissions, 1111)

        }
        return true
    }
}








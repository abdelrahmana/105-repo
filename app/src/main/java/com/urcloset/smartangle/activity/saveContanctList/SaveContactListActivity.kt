package com.urcloset.smartangle.activity.saveContanctList


import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.model.project_105.ContactModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.Constants.PERMISSIONS_REQUEST_READ_CONTACTS
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_save_contact_list.*
import kotlinx.android.synthetic.main.toolbar_backpress1.*

class SaveContactListActivity : TemplateActivity() {
    private lateinit var disposable: Disposable

    lateinit var ivBack:ImageView
    lateinit var cardNext:CardView;
    override fun set_layout() {
        setContentView(R.layout.activity_save_contact_list)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {


        ivBack=iv_back
        cardNext=btn_next

    }

    override fun init_events() {



        ivBack.setOnClickListener {
            BasicTools.exitActivity(this@SaveContactListActivity)
        }

        cardNext.setOnClickListener {
            retrieveContactsList()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,

                                            grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                retrieveContactsList()
            } else {
                //  toast("Permission must be granted in order to display contacts information")
            }
        }
    }

    override fun set_fragment_place() {

    }

    private fun retrieveContactsList() {

        var list=ArrayList<ContactModel>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS)
            //callback onRequestPermissionsResult
        } else {

            disposable = getContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    var contactsList = it
                    if (contactsList.isNotEmpty()) {

                        list.addAll(contactsList)
                        for (i in contactsList) {

                            Log.i("TEST_TEST", "${i.toString()}")
                        }


                    }
                }



        }
    }

    private fun getContacts(): Observable<MutableList<ContactModel>> {
        return Observable.create { emitter ->
            val list: MutableList<ContactModel> = ArrayList()
            val contentResolver: ContentResolver = contentResolver

            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.CUSTOM_RINGTONE,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )
            val cursor =
                contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER + ">0 AND LENGTH(" + ContactsContract.CommonDataKinds.Phone.NUMBER + ")>0",
                    null,
                    "display_name ASC"
                )

            if (cursor != null && cursor.count > 0) {

                var lastHeader = ""
                while (cursor.moveToNext()) {
                    val id =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                    val person =
                        ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI,
                            id.toLong()
                        )
                    val ring =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CUSTOM_RINGTONE))
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val mobileNumber =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                //    val header = getFirstChar(name)
              /*      if (header != lastHeader) {
                        lastHeader = header
                        list.add(
                            ContactItemViewState(
                                header,
                                View.GONE,
                                View.VISIBLE,
                                "",
                                name,
                                "",
                                "",
                                null
                            )
                        )
                    }*/

                    list.add(
                        ContactModel(
                            name,
                            mobileNumber

                        )
                    )
                }
                cursor.close()
            }
            emitter.onNext(list)
            emitter.onComplete()
        }
    }


}

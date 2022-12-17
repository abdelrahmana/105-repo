package com.urcloset.smartangle.tools



import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.urcloset.smartangle.activity.homeActivity.HomeViewModel
import com.urcloset.smartangle.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


abstract class TemplateFragment : Fragment(), CoroutineScope {
    val viewModelHome : HomeViewModel by activityViewModels()
    protected var parent: TemplateActivity?=null
    protected var snackbar: Snackbar? = null
    private lateinit var job: Job

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parent = context as TemplateActivity
    }


    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        BasicTools.setLanguagePerActivity(requireActivity(),null)




    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    fun init(savedInstanceState: Bundle?) {
        init_views()
        init_events()
        init_fragment(savedInstanceState)
    }

    open fun init_views()
    {}


    abstract fun init_events()





    abstract fun init_fragment(savedInstanceState: Bundle?)

    abstract fun on_back_pressed(): Boolean

    fun init_spinner() {}

    fun show_options() {}

    open fun change_user(user: User){}

   open fun cancel() {
        removeSnackbar()
    }

    fun removeSnackbar() {
        if (snackbar != null && snackbar!!.isShown())
            snackbar!!.dismiss()
    }
}

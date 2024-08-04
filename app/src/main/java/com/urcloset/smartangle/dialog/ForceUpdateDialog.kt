package com.urcloset.smartangle.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.urcloset.smartangle.databinding.UpdateDialogBinding
import com.urcloset.smartangle.globals.ForceUpdateChecker.Companion.keyForceUpdate
import com.urcloset.smartangle.tools.BasicTools.updateApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForceUpdateDialog : AppCompatDialogFragment() {

    lateinit var binding : UpdateDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UpdateDialogBinding.inflate(layoutInflater, container, false)
        binding.root.also {
            isCancelable = arguments?.getBoolean(keyForceUpdate)?:false//arguments?.getBoolean(CAN_BE_DISMISSED_EXTRA_KEY, true) ?: true
        }
        binding.cancel.visibility = if (isCancelable) View.VISIBLE else View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        //dialog?.setCancelable(arguments?.getBoolean(keyForceUpdate)?:false)
        binding.run {
            //tvTitle.text = arguments?.getString(TITLE_EXTRA_KEY)
           // tvMessage.text = arguments?.getString(MESSAGE_EXTRA_KEY)
           /* val whichSelectionAction = WhichSelectionAction.values().find {
                it.ordinal == arguments?.getInt(WHICH_SELECTION_INDEX)
            }*/
            //binding.message.setText(getString(R.string.are_u_sure_to_do_this_action,arguments?.getString(TITLE_EXTRA_KEY)?:"",getString(whichSelectionAction!!.resourceId)))
            cancel.setOnClickListener { // should go to dial
                dismiss()
            }

            updateApp.run {
              //  text = arguments?.getString(ACTION_TITLE_EXTRA_KEY)?:getString(R.string.confirm_action)
                setOnClickListener {
                    updateApp(requireContext())
                    /*requireActivity().supportFragmentManager.setFragmentResult(
                        ACTION_BUY,
                        bundleOf(
                            WHICH_SELECTION_INDEX to
                                    arguments?.getInt(WHICH_SELECTION_INDEX) // return true if not needed action
                        ) // in case got it
                    )*/
                   // dismiss()
                }
            }
        }
    }

    companion object {
        private const val TITLE_EXTRA_KEY = "title_extra_key"
        private const val MESSAGE_EXTRA_KEY = "message_extra_key"
        private const val CAN_BE_DISMISSED_EXTRA_KEY = "can_be_dismissed_extra_key"
        const val ACTION_TITLE_EXTRA_KEY = "action_title_extra_key"
        const val ACTION_BUY = "ACTION_BUY"
        const val WHICH_SELECTION_INDEX = "WHICH_SELECTION_INDEX"

        fun show(
            title: String,
            message: String,
            actionTitle: String,
            fragmentManager: FragmentManager,
            canBeDismissed: Boolean = true,
            whichSelectionIndex : Int = 0
        ) {
            newInstance(
                title,
                message,
                actionTitle,
                canBeDismissed,
                whichSelectionIndex
            ).show(
                fragmentManager,
                ForceUpdateDialog::class.java.canonicalName
            )
        }

        private fun newInstance(
            title: String,
            message: String,
            actionTitle: String,
            canBeDismissed: Boolean,
            whichSelectionIndex: Int,
        ) =
            ForceUpdateDialog().apply {
                arguments = bundleOf(
                    TITLE_EXTRA_KEY to title,
                    MESSAGE_EXTRA_KEY to message,
                    CAN_BE_DISMISSED_EXTRA_KEY to canBeDismissed,
                    ACTION_TITLE_EXTRA_KEY to actionTitle,
                    WHICH_SELECTION_INDEX to whichSelectionIndex
                )
            }
    }
}
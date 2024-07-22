package com.urcloset.smartangle.fragment.setting_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.urcloset.smartangle.R
import com.urcloset.smartangle.databinding.ConfirmActionLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmActionBottomSheet : AppCompatDialogFragment() {

    lateinit var binding : ConfirmActionLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConfirmActionLayoutBinding.inflate(layoutInflater, container, false)
        binding.root.also {
            isCancelable = arguments?.getBoolean(CAN_BE_DISMISSED_EXTRA_KEY, true) ?: true
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
       // dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        binding.run {
            //tvTitle.text = arguments?.getString(TITLE_EXTRA_KEY)
           // tvMessage.text = arguments?.getString(MESSAGE_EXTRA_KEY)
            binding.message.setText(getString(R.string.are_u_sure_to_do_this_action))
            cancel.setOnClickListener { // should go to dial
                dismiss()
            }

            requestFriend.run {
                text = arguments?.getString(ACTION_TITLE_EXTRA_KEY)?:getString(R.string.confirm_action)
                setOnClickListener {
                    requireActivity().supportFragmentManager.setFragmentResult(
                        ACTION_DELETE,
                        bundleOf(
                            WHICH_SELECTION_INDEX to
                                    arguments?.getInt(WHICH_SELECTION_INDEX) // return true if not needed action
                        ) // in case got it
                    )
                    dismiss()
                }
            }
        }
    }

    companion object {
        private const val TITLE_EXTRA_KEY = "title_extra_key"
        private const val MESSAGE_EXTRA_KEY = "message_extra_key"
        private const val CAN_BE_DISMISSED_EXTRA_KEY = "can_be_dismissed_extra_key"
        const val ACTION_TITLE_EXTRA_KEY = "action_title_extra_key"
        const val ACTION_DELETE = "ACTION_DELETE"
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
                ConfirmActionBottomSheet::class.java.canonicalName
            )
        }

        private fun newInstance(
            title: String,
            message: String,
            actionTitle: String,
            canBeDismissed: Boolean,
            whichSelectionIndex: Int,
        ) =
            ConfirmActionBottomSheet().apply {
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
package me.alberto.tellerium.screens.common.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.alberto.tellerium.R

class ConfirmDialog : DialogFragment() {

    private var listener: ConfirmListener? = null
    private var title: String? = null
    private var message: String? = null
    private var has_negative_btn: Boolean? = null
    private var buttonText: String? = null
    private var negButtonText: String? = null


    companion object {

        private val TITLE = "title"
        private val MESSAGE = "message"
        private val HAS_NEGATIVE_BTN = "has_nagative_btn"
        private val BUTTON_TEXT = "btn_text"
        private val NEG_TEXT = "neg_text"

        fun newInstance(
            listener: ConfirmListener,
            title: String,
            message: String,
            has_neg: Boolean = false,
            btnText: String,
            negBtnText: String? = null
        ): ConfirmDialog {
            val fragment = ConfirmDialog()
            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(MESSAGE, message)
            bundle.putString(BUTTON_TEXT, btnText)
            bundle.putBoolean(HAS_NEGATIVE_BTN, has_neg)
            bundle.putString(NEG_TEXT, negBtnText)
            fragment.arguments = bundle
            fragment.listener = listener
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(TITLE)
        message = arguments?.getString(MESSAGE)
        has_negative_btn = arguments?.getBoolean(HAS_NEGATIVE_BTN)
        buttonText = arguments?.getString(BUTTON_TEXT)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(buttonText) { _, _ ->
            listener?.onConfirm()
            dismiss()
        }
        has_negative_btn?.let {
            if (it) {
                builder.setNegativeButton(negButtonText ?: getString(R.string.cancel)) { _, _ ->
                    dismiss()
                }
            }
        }
        return builder.create()
    }


    interface ConfirmListener {
        fun onConfirm()
    }

}
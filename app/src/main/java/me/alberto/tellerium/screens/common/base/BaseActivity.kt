package me.alberto.tellerium.screens.common.base

import androidx.appcompat.app.AppCompatActivity
import me.alberto.tellerium.screens.common.dialogs.ConfirmDialog

abstract class BaseActivity :
    AppCompatActivity() {

    protected fun showDilaog(
        listener: () -> Unit,
        title: String,
        message: String,
        has_neg: Boolean,
        btnText: String,
        negBtnText: String?
    ) {
        val dialog = ConfirmDialog.newInstance(object : ConfirmDialog.ConfirmListener {
            override fun onConfirm() {
                listener()
            }
        }, title, message, has_neg, btnText, negBtnText)
        dialog.show(supportFragmentManager, dialog.javaClass.name)
    }


}
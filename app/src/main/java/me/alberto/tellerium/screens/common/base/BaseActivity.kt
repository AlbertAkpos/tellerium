package me.alberto.tellerium.screens.common.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import me.alberto.tellerium.R.string
import me.alberto.tellerium.screens.common.dialogs.ConfirmDialog
import permissions.dispatcher.PermissionRequest

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


    protected fun showPermissionsRationale(request: PermissionRequest) {
        val dialogFragment = ConfirmDialog.newInstance(
            title = getString(string.allow_permissions),
            message = getString(string.permission_rationale),
            btnText = getString(android.R.string.ok),
            listener = object : ConfirmDialog.ConfirmListener {
                override fun onConfirm() {
                    request.proceed()
                }
            })
        dialogFragment.show(supportFragmentManager, dialogFragment.javaClass.name)
    }

    protected fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }





}
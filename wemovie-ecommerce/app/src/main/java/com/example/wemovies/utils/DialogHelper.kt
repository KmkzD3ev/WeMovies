// com.example.wemovies.utils.DialogHelper.kt
package com.example.wemovies.utils

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.example.wemovies.R

/**
 * Dialog Personalisada
 * Efeito de Login
 */

object DialogHelper {
    private var dialog: Dialog? = null

    fun showLoadingDialog(context: Context) {
        if (dialog?.isShowing == true) return

        dialog = Dialog(context, R.style.TransparentDialogTheme)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_loading)

        dialog?.window?.apply {
            setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
            attributes = attributes.also { it.y = 90 }
        }

        dialog?.setCancelable(false)
        dialog?.show()
    }


    fun dismissLoadingDialog() {
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }
}

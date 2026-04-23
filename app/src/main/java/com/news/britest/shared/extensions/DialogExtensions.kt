package com.news.britest.shared.extensions

import android.app.Activity
import android.app.AlertDialog

fun Activity.showDialogError(title: String, message: String) {
    if (!isFinishing) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { _, _ ->
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}

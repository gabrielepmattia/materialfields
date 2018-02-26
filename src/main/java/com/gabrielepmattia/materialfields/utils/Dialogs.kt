package com.gabrielepmattia.materialfields.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.Html
import android.view.LayoutInflater
import com.gabrielepmattia.materialfields.R

/**
 * Created by gabry3795 on 26/02/2018.
 */

object Dialogs {
    fun showDialog(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
                .setMessage(message)
                .show()
    }

    fun showDialogWithPButton(context: Context, title: String, message: String,
                              positiveLabel: String,
                              positiveAction: DialogInterface.OnClickListener) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveLabel, positiveAction)
                .show()
    }

    fun showDialogWithPNButton(context: Context, title: String, message: String,
                               positiveLabel: String, negativeLabel: String,
                               positiveAction: DialogInterface.OnClickListener,
                               negativeAction: DialogInterface.OnClickListener) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveLabel, positiveAction)
                .setNegativeButton(negativeLabel, negativeAction)
                .show()
    }

    fun showDialogWithInputAndPNButtons(context: Context,
                                        inflater: LayoutInflater,
                                        title: String,
                                        positiveLabel: String,
                                        negativeLabel: String,
                                        positiveAction: DialogInterface.OnClickListener,
                                        negativeAction: DialogInterface.OnClickListener) {

        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        val dialogInputView = inflater.inflate(R.layout.dialog_input_text, null)
        builder.setView(dialogInputView)
        builder.setPositiveButton(positiveLabel, positiveAction)
        builder.setNegativeButton(negativeLabel, negativeAction)
        builder.show()
    }


    fun setHtmlMessage(builder: AlertDialog.Builder, s: String) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.setMessage(Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY))
        } else {
            builder.setMessage(Html.fromHtml(s))
        }
    }
}

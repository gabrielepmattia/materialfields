/*
 * Materialfields
 * Copyright (c) 2020 by gabrielepmattia, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.gabrielepmattia.materialfields.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.Html
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import com.gabrielepmattia.materialfields.R

object Dialogs {
    fun showDialog(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
            .setMessage(message)
            .show()
    }

    fun showDialogWithPButton(
        context: Context, title: String, message: String,
        positiveLabel: String,
        positiveAction: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveLabel, positiveAction)
            .show()
    }

    fun showDialogWithPNButton(
        context: Context, title: String, message: String,
        positiveLabel: String, negativeLabel: String,
        positiveAction: DialogInterface.OnClickListener,
        negativeAction: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveLabel, positiveAction)
            .setNegativeButton(negativeLabel, negativeAction)
            .show()
    }

    @SuppressLint("InflateParams")
    fun showDialogWithInputAndPNButtons(
        context: Context,
        inflater: LayoutInflater,
        title: String?,
        positiveLabel: String,
        negativeLabel: String,
        positiveAction: DialogInterface.OnClickListener,
        negativeAction: DialogInterface.OnClickListener,
        defaultText: String?
    ) {

        val builder = AlertDialog.Builder(context)
        builder.setTitle(title ?: Dialogs::class.java.simpleName)
        val dialogInputView = inflater.inflate(R.layout.dialog_input_text, null)

        // Set the default text to edittext
        val editText: EditText = dialogInputView.findViewById(R.id.dialog_input_edittext)
        if (defaultText != null) {
            editText.setText(defaultText)
            editText.setSelection(defaultText.length)
        }

        builder.setView(dialogInputView)
        builder.setPositiveButton(positiveLabel, positiveAction)
        builder.setNegativeButton(negativeLabel, negativeAction)
        val dialog = builder.create()
        // force keyboard to be displayed
        editText.requestFocus()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }

    fun showDialogWithOptions(
        context: Context,
        title: String?,
        options: Array<CharSequence>,
        action: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setItems(options, action)
        builder.show()
    }


    @Suppress("DEPRECATION")
    fun setHtmlMessage(builder: AlertDialog.Builder, s: String) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.setMessage(Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY))
        } else {
            builder.setMessage(Html.fromHtml(s))
        }
    }
}

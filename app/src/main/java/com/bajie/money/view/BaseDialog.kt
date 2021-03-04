package com.bajie.money.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

/**

 * bajie on 2021/3/4 14:08

 */
class BaseDialog(private val activity:Activity) : DialogFragment() {
    internal lateinit var listener: DialogListener;
    var mMsg: String = "";
    var mPositive: String = "确定";
    var mNegative: String = "取消";
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            val builder = AlertDialog.Builder(it);
            builder.setMessage(mMsg)
                .setPositiveButton(mPositive, DialogInterface.OnClickListener { _, _ ->
                    listener?.let { it.onDialogPositiveClick(this) }
                    dismiss();
                })
                .setNegativeButton(mNegative, DialogInterface.OnClickListener { _, _ ->
                    listener?.let { it.onDialogNegativeClick(this) }
                    dismiss();
                })

            builder.create();
        }
    }

    fun setMessage(msg: String): BaseDialog {
        mMsg = msg;
        return this;
    }

    fun setPositiveBtn(str: String): BaseDialog {
        mPositive = str;
        return this;
    }
    fun setNegativeBtn(str: String): BaseDialog {
        mNegative = str;
        return this;
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = activity as DialogListener;
        } catch (e: ClassCastException) {

        }
    }
}

interface DialogListener {
    fun onDialogPositiveClick(dialog: DialogFragment);
    fun onDialogNegativeClick(dialog: DialogFragment);
}

data class DialogData(val msg: String?, val positiveStr: String?="确认", val negativeStr: String?="取消");
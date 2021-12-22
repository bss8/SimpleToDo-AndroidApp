package com.example.simpletodo

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment

/**
 * Created using this guide:
 * https://developer.android.com/guide/topics/ui/dialogs
 */
class DeleteDialogFragment : DialogFragment() {
    // Use this instance of the interface to deliver action events
    internal lateinit var listener: NoticeDialogListener

    /* The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event callbacks.
    * Each method passes the DialogFragment in case the host needs to query it. */
    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            // dialog and id are required by the function signature
            // DialogInterface.OnClickListener(dialog, id)
            // as they are unused, we can write them as _
            builder.setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete") { _, _ ->
                    listener.onDialogPositiveClick(this)
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    // User cancelled the dialog
                    listener.onDialogNegativeClick(this)
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
package com.example.simpletodo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity(),
    DeleteDialogFragment.NoticeDialogListener {
    // values are variables that we won't change again,
    // variables can be reassigned
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter
    var itemPositionForDeletion : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            // need to pass in view to manipulate background color
            // this will be used to give the user a visual cue that the item will be deleted
            override fun onItemLongClicked(view: View, position: Int) {
                // 1. Record the item position that will be deleted
                itemPositionForDeletion = position

                // 1. Show a dialog with pos and neg options for deletion of a task
                showNoticeDialog()


                // 1. remove item from the list
                //listOfTasks.removeAt(position)
                // 2. Notify the adapter that our data set changed
                //adapter.notifyItemRemoved(position)
                //saveItems()
            }
        }

        val onClickListener = object : TaskItemAdapter.OnClickListener {
            override fun onItemClicked(position: Int) {
                // first parameter is the context, second is the class of the activity to launch
                val i = Intent(this@MainActivity, EditActivity::class.java)
                i.putExtra("taskDescription", listOfTasks[position])
                startActivity(i) // brings up the second activity
            }
        }

        loadItems()

        // look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener, onClickListener);
        // attach adapter to the recyclerView to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField);

        // set up the btn and input field, so users can enter a task
        // and add it to the list
        findViewById<Button>(R.id.addButton).setOnClickListener {
            Log.i("AddBtn", "User clicked on the add item button.")
            // 1. Grab the user added text
            val userInput = inputTextField.text.toString()

            // 2. Add the string to our list of tasks
            listOfTasks.add(userInput)
            // notify adapter that our data was updated (at end of list)
            Log.i("size", "${listOfTasks.size}")

            adapter.notifyItemInserted(listOfTasks.size - 1)

            // 3. reset the text input field
            inputTextField.setText("")

            saveItems()
        }
    } // end onCreate()

    // Save the data that the user added
    // by writing and reading from a file

    // Get the file we need
    fun getDataFile(): File {
        // Every line is going to represent a task in our list
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Save items by writing them into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    fun deleteItem() {
        // 1. remove item from the list
        listOfTasks.removeAt(itemPositionForDeletion)
        // 2. Notify the adapter that our data set changed
        adapter.notifyItemRemoved(itemPositionForDeletion)
        saveItems()
    }

    fun showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        val dialog = DeleteDialogFragment()
        dialog.show(supportFragmentManager, "DeleteDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        Log.i("DeletingTask", "Deleting selected task at position: $itemPositionForDeletion")
        deleteItem()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Log.i("DeleteCancel", "User cancelled the delete operation, task will not be removed.")
    }

}
package com.example.simpletodo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    // values are variables that we won't change again,
    // variables can be reassigned
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // 1. remove item from the list
                listOfTasks.removeAt(position)

                // 2. Notify the adapter that our data set changed
                adapter.notifyItemRemoved(position)

                saveItems()
            }
        }

        val onClickListener = object : TaskItemAdapter.OnClickListener {
            override fun onItemClicked(position: Int) {
                // first parameter is the context, second is the class of the activity to launch
                val i = Intent(this@MainActivity, EditActivity::class.java)
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
        findViewById<Button>(R.id.button).setOnClickListener {
            Log.i("AddBtn", "User clicked on the add item button.")
            // 1. Grab the user added text
            val userInput = inputTextField.text.toString()

            // 2. Add the string to our list of tasks
            listOfTasks.add(userInput)
            // notify adapter that our data was updated (at end of list)
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // 3. reset the text input field
            inputTextField.setText("")

            saveItems()
        }
    }

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
}
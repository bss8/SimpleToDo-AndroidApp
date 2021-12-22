package com.example.simpletodo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val editTaskField = findViewById<EditText>(R.id.editTaskItemText)
        val taskDescription = intent.getStringExtra("taskDescription")
        val position = intent.getIntExtra("position", -1)
        editTaskField.setText(taskDescription)

        findViewById<Button>(R.id.saveEditBtn).setOnClickListener {
            val data = Intent()
            data.putExtra("updatedTask", editTaskField.text.toString())
            data.putExtra("position", position)
            setResult(RESULT_OK, data)
            finish()
        }

        findViewById<Button>(R.id.cancelEditBtn).setOnClickListener {
            Log.i("CancelledEdit", "User cancelled editing the task.")
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
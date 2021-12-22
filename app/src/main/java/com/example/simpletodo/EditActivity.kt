package com.example.simpletodo

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val taskDescription = intent.getStringExtra("taskDescription")
        findViewById<EditText>(R.id.editTaskItemText).setText(taskDescription)
    }
}
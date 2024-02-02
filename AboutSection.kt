package com.example.datastore

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AboutSection(studentName: String, studentId: String) {
    Column {
        Text(
            text = "About",
            style = MaterialTheme.typography.h6
        )

        Text("Student Name: $studentName")
        Text("Student ID: $studentId")
    }
}

package com.example.datastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.datastore.data.UserDataStore
//import com.example.datastore.data.UserStore
import com.example.datastore.ui.theme.DataStoreTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataStoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Create DataStore instance
    val dataStore = UserDataStore(context)

    // State variables for input fields
    val usernameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val idState = remember { mutableStateOf("") }

    // Load data from DataStore on initial launch
    LaunchedEffect(true) {
        dataStore.readUserData().collect { userData ->
            usernameState.value = userData.username
            emailState.value = userData.email
            idState.value = userData.id
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Input fields
        TextField(
            value = usernameState.value,
            onValueChange = { usernameState.value = it },
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = idState.value,
            onValueChange = { idState.value = it },
            label = { Text("Id") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                // Save data to DataStore
                CoroutineScope(Dispatchers.IO).launch {
                    // Save data to DataStore
                    dataStore.saveUserData(
                        username = usernameState.value,
                        email = emailState.value,
                        id = idState.value
                    )
                }
            }) {
                Text("Save")
            }

            Button(onClick = {
                // Start a coroutine scope
                CoroutineScope(Dispatchers.IO).launch {
                    // Load data from DataStore
                    dataStore.readUserData().collect { userData ->
                        usernameState.value = userData.username
                        emailState.value = userData.email
                        idState.value = userData.id
                    }
                }
            }) {
                Text("Load")
            }

            Button(onClick = {
                // Start a coroutine scope
                CoroutineScope(Dispatchers.IO).launch {
                    // Clear data from DataStore
                    dataStore.clearUserData()
                    usernameState.value = ""
                    emailState.value = ""
                    idState.value = ""
                }
            }) {
                Text("Clear")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // About section
            AboutSection(studentName = "Your Name", studentId = "Your Student ID")
        }
    }
}
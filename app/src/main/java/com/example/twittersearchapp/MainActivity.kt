package com.example.twittersearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.twittersearchapp.ui.theme.TwitterSearchAppTheme
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.items
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TwitterSearchAppTheme {
                TwitterSearchScreen()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwitterSearchScreen() {

    var searchQuery by remember { mutableStateOf("") }
    var tagInput by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf(listOf("AndroidFP", "Google", "JavaFP")) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Twitter Search App") }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Enter Twitter search query here") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = tagInput,
                onValueChange = { tagInput = it },
                label = { Text("Tag your query") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (tagInput.isNotBlank()) {
                        tags = tags + tagInput
                        tagInput = ""
                    }
                }
            ) {
                Text("Save")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Tagged Searches",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(tags) { tag ->
                    TagItem(tag)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { tags = emptyList() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear Tags")
            }
        }
    }
}
@Composable
fun TagItem(tag: String) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Button(onClick = {}) {
            Text(tag)
        }

        Button(onClick = {}) {
            Text("Edit")
        }
    }
}
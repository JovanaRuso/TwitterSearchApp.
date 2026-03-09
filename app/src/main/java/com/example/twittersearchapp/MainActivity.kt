package com.example.twittersearchapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.twittersearchapp.ui.theme.TwitterSearchAppTheme
import java.io.BufferedReader
import java.io.InputStreamReader

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

fun loadDictionary(context: Context): Map<String, String> {

    val dictionary = mutableMapOf<String, String>()

    val inputStream = context.assets.open("dictionary.txt")
    val reader = BufferedReader(InputStreamReader(inputStream))

    reader.forEachLine { line ->

        val parts = line.split(",")

        if (parts.size == 2) {
            val english = parts[0].trim()
            val macedonian = parts[1].trim()

            dictionary[english] = macedonian
            dictionary[macedonian] = english
        }
    }

    reader.close()

    return dictionary
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwitterSearchScreen() {

    val context = LocalContext.current
    val dictionary = remember { loadDictionary(context) }

    var searchWord by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    var tagInput by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf(listOf("AndroidFP", "Google", "JavaFP")) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dictionary Search App") }
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
                value = searchWord,
                onValueChange = { searchWord = it },
                label = { Text("Search word (English or Macedonian)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {

                    val word = searchWord.trim()

                    result = dictionary[word] ?: "Word not found"
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = result,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

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
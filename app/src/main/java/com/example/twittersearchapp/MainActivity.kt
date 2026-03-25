package com.example.twittersearchapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.twittersearchapp.ui.theme.TwitterSearchAppTheme
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TwitterSearchAppTheme {
                DictionaryScreen()
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
fun DictionaryScreen() {

    val context = LocalContext.current
    val dictionary = remember { loadDictionary(context) }

    var searchWord by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    var tagInput by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf(listOf("AndroidFP", "Google", "JavaFP")) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Dictionary App",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Поле за внесување збор
            OutlinedTextField(
                value = searchWord,
                onValueChange = { searchWord = it },
                label = { Text("Search word (English or Macedonian)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Копче за пребарување со модерен стил
            Button(
                onClick = {
                    val word = searchWord.trim()
                    result = dictionary[word] ?: "Word not found"
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE),
                    contentColor = Color.White
                )
            ) {
                Text("Search")
            }

            // Резултат во Card
            if (result.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Result: $result",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Поле за тагови
            OutlinedTextField(
                value = tagInput,
                onValueChange = { tagInput = it },
                label = { Text("Tag your query") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (tagInput.isNotBlank()) {
                        tags = tags + tagInput
                        tagInput = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF03DAC5),
                    contentColor = Color.Black
                )
            ) {
                Text("Save Tag")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Tagged Searches",
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(tags) { tag ->
                    TagItem(tag)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { tags = emptyList() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Clear Tags")
            }
        }
    }
}

@Composable
fun TagItem(tag: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {},
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        ) {
            Text(tag)
        }

        Button(
            onClick = {},
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow, contentColor = Color.Black)
        ) {
            Text("Edit")
        }
    }
}
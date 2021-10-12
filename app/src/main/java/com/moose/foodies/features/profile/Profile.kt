package com.moose.foodies.features.profile

import android.content.Intent
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.moose.foodies.features.add.AddActivity

@Composable
fun Profile() {
    Scaffold(backgroundColor = colors.primary, floatingActionButton = { Fab() }) {

    }
}

@Composable
fun Fab() {
    val context = LocalContext.current
    val intent = Intent(context, AddActivity::class.java)
    FloatingActionButton(onClick = { context.startActivity(intent) }) {
        Icon(Icons.Default.Add, contentDescription = "add icon")
    }
}
package ru.absolutelee.vknewsclient.samples

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun ActivityResultTest() {

    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))

    Column(modifier = Modifier.fillMaxSize()) {


        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                context.startActivity(intent)
            }
        ) {
            Text(text = "Bebra")
        }
    }
}
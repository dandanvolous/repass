package me.dandanvolous.repass.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RepassLogo(modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = "Repass",
            modifier = Modifier.align(Alignment.Start),
            style = MaterialTheme.typography.displayLarge,
        )
        Spacer(Modifier.size(16.dp))
        Text(
            text = "Pass ${Typography.mdash} generate",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "...re-generate",
            modifier = Modifier.align(Alignment.End),
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}
package me.dandanvolous.repass.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RepassDocumentItem(
    name: String,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = { Text(name) }
    )
    Row(modifier.padding(8.dp)) {
        Text(
            text = name,
            modifier = Modifier.weight(1.0F),
            style = MaterialTheme.typography.titleSmall,
        )
        Spacer(Modifier.size(16.dp))
        TextButton(
            onClick = {  },
        ) {
            Text("Edit")
        }
    }
}
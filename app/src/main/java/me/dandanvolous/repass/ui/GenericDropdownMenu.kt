package me.dandanvolous.repass.ui

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> GenericDropdownMenu(
    current: T?,
    onCurrentChange: (T) -> Unit,
    fullList: List<T>,
    string: @Composable (T) -> String,
    modifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null,
) {
    val currentString = if (current != null) string(current) else ""

    val (expanded, expand) = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = expand,
        modifier = modifier
            .width(IntrinsicSize.Min),
    ) {
        TextField(
            value = currentString,
            onValueChange = {  },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            label = label,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expand(false) },
        ) {
            fullList.forEach { changed ->
                val changedString = string(changed)
                DropdownMenuItem(
                    text = { Text(changedString) },
                    onClick = {
                        onCurrentChange(changed)
                        expand(false)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}
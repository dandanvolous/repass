package me.dandanvolous.repass.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource

@Immutable
@JvmInline
value class SrId(@StringRes val id: Int) {

    val string: String
        @ReadOnlyComposable
        @Composable
        get() = stringResource(id)

    val Context.string: String get() = getString(id)
}
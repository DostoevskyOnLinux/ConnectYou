package com.bnyro.contacts.ui.components.editor

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bnyro.contacts.R
import com.bnyro.contacts.obj.TranslatedType
import com.bnyro.contacts.obj.ValueWithType
import com.bnyro.contacts.ui.components.EditorEntry
import com.bnyro.contacts.ui.components.dialogs.DialogButton
import com.bnyro.contacts.util.CalendarUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerEditor(
    @StringRes label: Int,
    state: MutableState<ValueWithType>,
    types: List<TranslatedType>,
    onCreateNew: () -> Unit
) {
    var showPicker by remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState(
        runCatching {
            CalendarUtils.dateToMillis(state.value.value)
        }.getOrDefault(null)
    )

    LaunchedEffect(datePickerState.selectedDateMillis) {
        state.value.value = datePickerState.selectedDateMillis?.let {
            CalendarUtils.millisToDate(it.toString(), CalendarUtils.isoDateFormat)
        } ?: ""
    }

    EditorEntry(state = state, types = types, onCreateNew = onCreateNew) {
        Column(
            modifier = Modifier
                .weight(1f)
                .height(75.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    showPicker = true
                }
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(label),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = when (state.value.value) {
                    "" -> ""
                    else -> runCatching {
                        CalendarUtils
                            .millisToDate(datePickerState.selectedDateMillis.toString())
                            .substringBefore(" ")
                    }.getOrDefault(state.value.value)
                }
            )
        }

        if (showPicker) {
            AlertDialog(
                onDismissRequest = {
                    showPicker = false
                },
                dismissButton = {
                    DialogButton(text = stringResource(R.string.reset)) {
                        state.value.value = ""
                        showPicker = false
                    }
                },
                confirmButton = {
                    DialogButton(text = stringResource(R.string.okay)) {
                        showPicker = false
                    }
                },
                text = {
                    DatePicker(
                        datePickerState = datePickerState,
                        title = {}
                    )
                }
            )
        }
    }
}

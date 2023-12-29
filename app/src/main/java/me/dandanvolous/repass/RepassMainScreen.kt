package me.dandanvolous.repass

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.dandanvolous.repass.pass.passKey
import me.dandanvolous.repass.ui.GenericDropdownMenu
import me.dandanvolous.repass.ui.RepassLogo

private val FORM_INDEX_NEW = -1

private class RepassMainScreenInternalState(
    setFormSet: RepassFormSet = RepassFormSets["all"],
    setForms: List<RepassDocumentForm> = emptyList(),
    setValues: Map<String, String> = emptyMap(),
    formToEdit: Int = FORM_INDEX_NEW,
    newForm: RepassDocumentForm? = null,
    newValues: Map<String, String> = emptyMap(),
    field: RepassPasswordField? = null,
    iteration: Int = 0
) {

    var setFormSet by mutableStateOf(setFormSet)

    val setForms = mutableStateListOf<RepassDocumentForm>().apply { addAll(setForms) }

    val setValues = mutableStateMapOf<String, String>().apply { putAll(setValues) }

    var formToEdit by mutableIntStateOf(formToEdit)

    var newForm by mutableStateOf(newForm)

    val newValues = mutableStateMapOf<String, String>().apply { putAll(newValues) }

    val availableForms by derivedStateOf(structuralEqualityPolicy()) {
        val result = this.setFormSet.forms.toMutableSet()


        result.removeAll(this.setForms)
        this.newForm?.let { result.remove(it) }

        return@derivedStateOf result.toList()
    }

    var field by mutableStateOf(field)

    var iteration by mutableIntStateOf(iteration)

    var length by mutableIntStateOf(field?.length?.first ?: 8)

    val password by derivedStateOf {
        val fieldValue = this.field ?: return@derivedStateOf null

        val info = passInfo(this.setFormSet, this.setForms, this.setValues)
        val material = passMaterial(fieldValue)
        val alphabet = fieldValue.alphabet.value
        val assertion = fieldValue.assertion
        passKey(info, material, alphabet, length, assertion, this.iteration)
    }

    fun applyForm(form: RepassDocumentForm, values: Map<String, String>): Boolean {
        val hasForm = formToEdit != FORM_INDEX_NEW

        if (!hasForm)
            setForms.add(form)
        else {
            val oldForm = setForms[formToEdit]

            if (oldForm != form) {
                val fieldIds = oldForm.fields.map { it.id }

                setValues.keys.removeIf { it in fieldIds }
            }

            setForms[formToEdit] = form
        }

        setValues.putAll(values)

        return hasForm
    }

    fun revokeForm(form: RepassDocumentForm): Boolean {
        val hasForm = setForms.remove(form)

        if (hasForm) {
            val fieldIds = form.fields.map { it.id }

            setValues.keys.removeIf { it in fieldIds }

            return hasForm
        }

        return hasForm
    }

    // Use FORM_INDEX_NEW to prepare new form
    fun prepareEdit(formIndex: Int) {
        when (formIndex) {
            FORM_INDEX_NEW -> {
                formToEdit = formIndex
                newForm = null
                newValues.clear()
            }
            else -> {
                val form = setForms[formIndex]
                val values = form.fields.map { it.id to setValues[it.id]!! }

                formToEdit = formIndex
                newForm = form
                newValues.clear()
                newValues.putAll(values)
            }
        }
    }

    @JvmName("setNewFormManaged")
    fun setNewForm(form: RepassDocumentForm) {
        newForm = form
        newValues.clear()
        form.fields.forEach { newValues[it.id] = "" }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepassMainScreen(modifier: Modifier = Modifier) {
    val state = remember { RepassMainScreenInternalState() }

    val scope = rememberCoroutineScope()
    val secondScreen = rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = secondScreen)

    BottomSheetScaffold(
        sheetContent = {
            val scrollState = rememberScrollState()

            val isPartiallyExpanded by remember { derivedStateOf { secondScreen.targetValue == SheetValue.PartiallyExpanded } }
            LaunchedEffect(isPartiallyExpanded) {
                if (isPartiallyExpanded && scrollState.value != 0) scrollState.animateScrollTo(0)
            }

            Column(modifier = Modifier.verticalScroll(scrollState).padding(32.dp)) {
                GenericDropdownMenu(
                    current = state.newForm,
                    onCurrentChange = { state.setNewForm(it) },
                    fullList = state.availableForms,
                    string = { it.name.string },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.size(32.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    state.newForm?.fields?.forEach { form ->
                        TextField(
                            value = state.newValues[form.id]!!,
                            onValueChange = { state.newValues[form.id] = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(form.name.string) },
                        )
                    }
                }

                Spacer(Modifier.size(32.dp))

                TextButton(
                    onClick = { scope.launch { secondScreen.hide() } },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                    Text("Delete")
                }
                Spacer(Modifier.size(8.dp))
                OutlinedButton(
                    onClick = { scope.launch { secondScreen.hide() } },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Cancel", Modifier.rotate(-90f))
                    Text("Cancel")
                }
                Spacer(Modifier.size(8.dp))
                Button(
                    onClick = { scope.launch {
                        state.applyForm(checkNotNull(state.newForm), state.newValues)

                        secondScreen.hide()
                    } },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Default.Done, contentDescription = "Done")
                    Text("Done")
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 192.dp
    ) {
        Box(Modifier.fillMaxWidth()) {
            Column(Modifier.align(Alignment.Center).width(IntrinsicSize.Min)) {
                RepassLogo(Modifier.fillMaxWidth())

                Spacer(Modifier.size(16.dp))
                Divider(Modifier.fillMaxWidth())
                Spacer(Modifier.size(16.dp))

                GenericDropdownMenu(
                    current = state.setFormSet,
                    onCurrentChange = { state.setFormSet = it },
                    fullList = RepassFormSets,
                    string = { it.name.string },
                    label = { Text(stringResource(R.string.screen_mode)) },
                )

                Spacer(Modifier.size(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    state.setForms.forEachIndexed { index, form ->
                        ListItem(
                            headlineContent = { Text(form.name.string) },
                            modifier = Modifier.clickable { scope.launch {
                                state.prepareEdit(index)

                                secondScreen.partialExpand()
                            } }
                        )
                    }
                }

                Spacer(Modifier.size(16.dp))

                TextButton(
                    onClick = {
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.clear_screen))
                    Text(stringResource(R.string.clear_screen))
                }
                Spacer(Modifier.size(8.dp))
                Button(
                    onClick = { scope.launch {
                        state.prepareEdit(FORM_INDEX_NEW)

                        secondScreen.partialExpand()
                    } },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_document))
                    Text(stringResource(R.string.add_document))
                }

                Spacer(Modifier.size(16.dp))
                Divider(Modifier.fillMaxWidth())
                Spacer(Modifier.size(16.dp))

                GenericDropdownMenu(
                    current = state.field,
                    onCurrentChange = {
                        state.field = it
                        state.length = it.length.first
                    },
                    fullList = RepassPasswordFields,
                    string = { it.name.string },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Password field") }
                )

                Spacer(Modifier.size(16.dp))

                Text("Iteration")
                var iterationSlider by remember { mutableFloatStateOf(1f) }
                Slider(
                    value = iterationSlider,
                    onValueChange = { iterationSlider = it; println("iterations slider: $it") },
                    modifier = Modifier.fillMaxWidth(),
                    valueRange = 1f..5f,
                    steps = 3,
                    onValueChangeFinished = { state.iteration = iterationSlider.toInt() }
                )
                Spacer(Modifier.size(8.dp))
                Text("Length")
                var lengthSlider by remember(state.field) { mutableFloatStateOf(state.field?.length?.start?.toFloat() ?: 8f) }
                Slider(
                    value = lengthSlider,
                    onValueChange = { lengthSlider = it },
                    modifier = Modifier.fillMaxWidth(),
                    valueRange = state.field?.length?.let { it.first.toFloat()..it.last.toFloat() } ?: 8f..16f,
                    steps = state.field?.length?.let { it.last - it.first - 1 } ?: 7,
                    onValueChangeFinished = { state.length = lengthSlider.toInt() }
                )

                Spacer(Modifier.size(16.dp))

                TextField(
                    value = state.password ?: "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    label = { Text("Password") },
                    placeholder = { Text("Select password field") }
                )
            }
        }
    }
}
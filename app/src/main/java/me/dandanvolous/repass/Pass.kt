package me.dandanvolous.repass

/*

Info format

set_id:
  form_id_1:
    field_id_1: field_id_1_value
    field_id_2: field_id_2_value
    ...
    field_id_n: field_id_n_value
  form_id_2:
    field_id_1: field_id_1_value
    field_id_2: field_id_2_value
    ...
    field_id_n: field_id_n_value
  ...

 */


private fun measureInfoSize(
    set: RepassFormSet,
    forms: List<RepassDocumentForm>,
    values: Map<String, String>,
): Int {
    var result = set.id.length + 2 // id + colon + linebreak

    result += forms.size * 4 // 2 x indentation + colon + linebreak
    forms.forEach { form ->
        result += form.id.length

        result += form.fields.size * 7 // 4 x indentation + colon + space + linebreak
        form.fields.forEach { field ->
            result += field.id.length
            result += values[field.id]!!.length
        }
    }

    return result
}

fun passInfo(
    set: RepassFormSet,
    forms: List<RepassDocumentForm>,
    values: Map<String, String>
): String = buildString(measureInfoSize(set, forms, values)) {
    append(set.id)
    append(":\n")

    forms.forEach { form ->
        append("  ")
        append(form.id)
        append(":\n")

        form.fields.forEach { field ->
            append("    ")
            append(field.id)
            append(": ")
            append(values[field.id]!!)
            append("\n")
        }
    }
    deleteAt(lastIndex)
}

fun passMaterial(field: RepassPasswordField): String = field.id
package me.dandanvolous.repass

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import me.dandanvolous.repass.utils.SrId

@Immutable
data class RepassDocumentForm(
    val id: String,
    val name: SrId,
    val fields: List<RepassDocumentField>
)

fun RepassDocumentForm(id: String, @StringRes nameRes: Int, fields: List<RepassDocumentField>) = RepassDocumentForm(id, SrId(nameRes), fields)

@Immutable
data class RepassDocumentField(
    val id: String,
    val name: SrId
)

fun RepassDocumentField(id: String, @StringRes nameRes: Int) = RepassDocumentField(id, SrId(nameRes))

@Immutable
data class RepassFormSet(
    val id: String,
    val name: SrId,
    val forms: List<RepassDocumentForm>
)

fun RepassFormSet(id: String, @StringRes nameRes: Int, forms: List<RepassDocumentForm>) = RepassFormSet(id, SrId(nameRes), forms)

@Stable
val RepassDocumentForms: List<RepassDocumentForm> = listOf(
    RepassDocumentForm(
        id = "passport_ru",
        nameRes = R.string.form_passport_ru,
        fields = listOf(
            RepassDocumentField(id = "passport_id", nameRes = R.string.form_passport_id),
            RepassDocumentField(id = "passport_no", nameRes = R.string.form_passport_no),
            RepassDocumentField(id = "passport_name", nameRes = R.string.form_passport_name),
            RepassDocumentField(id = "passport_sex", nameRes = R.string.form_passport_sex),
            RepassDocumentField(id = "passport_nationality", nameRes = R.string.form_passport_nationality),
            RepassDocumentField(id = "passport_birth_date", nameRes = R.string.form_passport_birth_date),
            RepassDocumentField(id = "passport_birth_place", nameRes = R.string.form_passport_birth_place),
            RepassDocumentField(id = "passport_authority", nameRes = R.string.form_passport_authority),
            RepassDocumentField(id = "passport_issue_date", nameRes = R.string.form_passport_issue_date),
            RepassDocumentField(id = "passport_issue_place", nameRes = R.string.form_passport_issue_place),
        )
    ),
    RepassDocumentForm(
        id = "visit_card",
        nameRes = R.string.form_visit_card,
        fields = listOf(
            RepassDocumentField(id = "number", nameRes = R.string.form_visit_card_number),
            RepassDocumentField(id = "email", nameRes = R.string.form_visit_card_email),
        )
    ),
)

private val RepassDocumentFormsMap = RepassDocumentForms.associateBy { it.id }

@Stable
operator fun List<RepassDocumentForm>.get(id: String): RepassDocumentForm {
    val result = if (this === RepassDocumentForms)
        RepassDocumentFormsMap[id]
    else
        find { it.id == id }
    return requireNotNull(result) { "No such id in the list." }
}

@Stable
val RepassFormSets: List<RepassFormSet> = listOf(
    RepassFormSet(
        id = "all",
        nameRes = R.string.set_all,
        forms = listOf(
            RepassDocumentForms["visit_card"],
            RepassDocumentForms["passport_ru"],
        )
    ),
    RepassFormSet(
        id = "ru",
        nameRes = R.string.set_ru,
        forms = listOf(
            RepassDocumentForms["passport_ru"],
            RepassDocumentForms["visit_card"],
        )
    ),
)

private val RepassFormSetsMap = RepassFormSets.associateBy { it.id }

@Stable
operator fun List<RepassFormSet>.get(id: String): RepassFormSet {
    val result = if (this === RepassFormSets)
        RepassFormSetsMap[id]
    else
        find { it.id == id }
    return requireNotNull(result) { "No such id in the list." }
}
package me.dandanvolous.repass

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import me.dandanvolous.repass.utils.SrId

@Immutable
data class RepassPasswordField(
    val id: String,
    val name: SrId,
    val alphabet: Alphabet,
    val length: IntRange,
    val assertion: Regex?,
)

@Immutable
class Alphabet(private val alphabets: List<String>) {

    constructor(vararg alphabets: String) : this(alphabets.toList())

    val length: Int by lazy { alphabets.fold(0) { acc, s -> acc + s.length } }

    val value: String by lazy { buildString(length) { alphabets.forEach { append(it) } } }
}

private const val LATIN_ALPHABET = """abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"""

@Stable
val RepassPasswordFields: List<RepassPasswordField> = listOf(
    RepassPasswordField(
        id = "gosuslugi",
        name = SrId(R.string.pass_gosuslugi),
        alphabet = Alphabet(LATIN_ALPHABET, """!"$%&'()+,-./:;<=>?@[]^_{|}~`"""),
        length = 8..16,
        assertion = null,
    ),
)
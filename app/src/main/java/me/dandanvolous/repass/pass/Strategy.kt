package me.dandanvolous.repass.pass

private val MATCH_ALL_ASSERTION = ".*".toRegex()

fun passKey(
    info: String,
    material: String,
    alphabet: String,
    length: Int,
    assertion: Regex? = null,
    iteration: Int = 0,
    tries: Int = 1000,
): String {
    require(iteration < 100)
    require(tries <= 1000)

    return passKey(
        info.encodeToByteArray(),
        material.encodeToByteArray(),
        alphabet.toCharArray(),
        length,
        assertion ?: MATCH_ALL_ASSERTION,
        iteration,
        tries
    )
}

@OptIn(ExperimentalStdlibApi::class)
private fun passKey(
    info: ByteArray,
    material: ByteArray,
    alphabet: CharArray,
    length: Int,
    assertion: Regex,
    iteration: Int,
    tries: Int
): String {
    repeat(tries) {
        val salt = salt(10007 * it + 117 * length + 7 * iteration)
        println("length: $length, try: $it, iteration: $iteration")
        println("salt: ${salt.toHexString()}")
        val secret = hkdf(material, info, salt, length)
        val password = translate(secret, alphabet)

        if (password matches assertion) return password
    }

    error("Unable to generate password under assertion.")
}

private fun salt(sequence: Int): ByteArray = ByteArray(32) { (sequence shl ((it % 4) * 8)).toByte() }

private fun translate(secret: ByteArray, alphabet: CharArray): String = buildString(secret.size) {
    secret.forEach { byte -> append(alphabet[(byte.toUByte().toInt() * alphabet.lastIndex) / UByte.MAX_VALUE.toInt()]) }
}
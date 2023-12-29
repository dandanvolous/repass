package me.dandanvolous.repass.pass

import me.dandanvolous.repass.hkdf.HKDF

val REPASS_HKDF_VERSION = 3

fun hkdf(material: ByteArray, info: ByteArray, salt: ByteArray, length: Int, version: Int = REPASS_HKDF_VERSION): ByteArray =
    HKDF.createFor(version).deriveSecrets(material, info, salt, length)
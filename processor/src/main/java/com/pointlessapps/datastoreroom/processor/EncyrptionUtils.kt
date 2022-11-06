package com.pointlessapps.datastoreroom.processor

import com.pointlessapps.datastoreroom.processor.Base64.decodeFromBase64
import com.pointlessapps.datastoreroom.processor.Base64.encodeToBase64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

fun String.encrypt(key: String): String = runCatching {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    val secretKeySpec = SecretKeySpec(key.toByteArray(), "AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
    val encrypted = cipher.doFinal(this.toByteArray())
    return@runCatching encrypted.encodeToBase64()
}.getOrElse { return@getOrElse this }

fun String.decrypt(key: String): String = runCatching {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    val secretKeySpec = SecretKeySpec(key.toByteArray(), "AES")
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
    val decrypted = cipher.doFinal(this.decodeFromBase64())
    return@runCatching decrypted.decodeToString()
}.getOrElse { return@getOrElse this }

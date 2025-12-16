package org.mavriksc.messin.random

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.crypto.spec.IvParameterSpec
import java.security.SecureRandom
import java.util.Base64

fun encrypt(plainText: String, key: ByteArray): String {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    val secretKey = SecretKeySpec(key, "AES")
    val iv = ByteArray(16)
    SecureRandom().nextBytes(iv)
    val ivParameterSpec = IvParameterSpec(iv)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
    val encryptedBytes = cipher.doFinal(plainText.toByteArray())
    return Base64.getEncoder().encodeToString(iv + encryptedBytes)
}

fun decrypt(encryptedText: String, key: ByteArray): String {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    val secretKey = SecretKeySpec(key, "AES")
    val decodedBytes = Base64.getDecoder().decode(encryptedText)
    val iv = decodedBytes.sliceArray(0..15)
    val cipherText = decodedBytes.sliceArray(16 until decodedBytes.size)
    cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
    val decryptedBytes = cipher.doFinal(cipherText)
    return String(decryptedBytes)
}

fun main() {
    val key = "MySecretEncryptionKey024".toByteArray() // Use a secure key generation method
    val plainText = "Hello, World!"
    val encryptedText = encrypt(plainText, key)
    val decryptedText = decrypt(encryptedText, key)

    println("Encrypted: $encryptedText")
    println("Decrypted: $decryptedText")
}
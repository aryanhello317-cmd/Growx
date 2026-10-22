package com.example.data.local

import java.security.MessageDigest

object SecurityHelper {
    private const val SALT = "BuildX_Secure_Salt_2026_Fitness"

    fun hashSecret(input: String): String {
        val bytes = (input + SALT).toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun verifySecret(input: String, storedHash: String): Boolean {
        if (storedHash.isBlank() || input.isBlank()) return false
        return hashSecret(input) == storedHash
    }
}

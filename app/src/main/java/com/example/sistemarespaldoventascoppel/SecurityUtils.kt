package com.example.sistemarespaldoventascoppel

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.security.SecureRandom
import android.util.Base64

object SecurityUtils {
    private const val PREFS_NAME = "secure_prefs"
    private const val KEY_DB_PASSPHRASE = "db_passphrase"

    fun getDatabasePassphrase(context: Context): String {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        val sharedPreferences = EncryptedSharedPreferences.create(
            PREFS_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        var passphrase = sharedPreferences.getString(KEY_DB_PASSPHRASE, null)
        if (passphrase == null) {
            passphrase = generateRandomPassphrase()
            sharedPreferences.edit().putString(KEY_DB_PASSPHRASE, passphrase).apply()
        }
        return passphrase
    }

    private fun generateRandomPassphrase(): String {
        val random = SecureRandom()
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}

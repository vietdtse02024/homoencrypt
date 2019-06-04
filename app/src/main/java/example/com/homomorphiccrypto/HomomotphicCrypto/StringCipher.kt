package example.com.homomorphiccrypto.HomomotphicCrypto

import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi
import android.util.Log
import java.math.BigInteger
import java.security.*
import java.security.spec.RSAKeyGenParameterSpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import javax.security.auth.x500.X500Principal

class StringCipher(context: Context, alias: String) {
    init {
//        generateKey(alias)
        generateKeySet(context, alias)
    }

    companion object {
        const val KEY_STORE = "AndroidKeyStore"
        const val FIXED_IV = "WT_INTIAL_IV"
    }

    //    private fun generateKey(alias: String) {
//        val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_HMAC_SHA256)
//        keyPairGenerator.initialize(genKeyGenParameterSpec(alias))
//        val kp = keyPairGenerator.generateKeyPair()
//        Log.d("SQLite","KQ|==>${kp.public.toString()}")
//    }
    private fun generateKeySet(context: Context, alias: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val keyPairGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEY_STORE)
            keyPairGenerator.init(genKeyParameterSpec23(alias))
            val kp = keyPairGenerator.generateKey()
            Log.d("SQLite", "KQ|KeyAPI23==>${kp.algorithm}")
        } else {
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA", KEY_STORE)
            keyPairGenerator.initialize(genKeyPairGeneratorSpec(context, alias))
            val kp = keyPairGenerator.generateKeyPair()
            Log.d("SQLite", "KQ|KeyAPI<23==>${kp.public.toString()}")
        }
    }

    //    fun genKeyGenParameterSpec(alias: String): KeyGenParameterSpec {
//        return KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
//            .setAlgorithmParameterSpec(RSAKeyGenParameterSpec(512,F4))//Supported sizes: 512, 768, 1024, 2048, 3072, 4096
//            .setDigests(KeyProperties.DIGEST_SHA256,KeyProperties.DIGEST_SHA384,KeyProperties.DIGEST_SHA512)
//            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
//            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
//            .setUserAuthenticationRequired(false)
//            .build()
//    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun genKeyParameterSpec23(alias: String): KeyGenParameterSpec {
        return KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(false)
                .build()
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun genKeyPairGeneratorSpec(context: Context, alias: String): KeyPairGeneratorSpec {
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 30)
        return KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSubject(X500Principal("CN=$alias"))
                .setAlgorithmParameterSpec(RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4))
                .setSerialNumber(BigInteger.TEN)
                .setStartDate(start.time)
                .setEndDate(end.time)
                .build()
    }

    private fun getKeyAES(alias: String): Key? {
        val keystore = KeyStore.getInstance(KEY_STORE).apply { load(null) }
        return keystore.getKey(alias, null)
    }

    fun encrypt(alias: String, data: String): String? {
        /*return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            encryptedStringByAES(alias, data)
        } else {

            (alias, data)
        }*/
        return encryptedStringByAES(alias, data)
    }

    fun decrypt(alias: String, data: String): String? {
        /*return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decryptedStringByAES(alias, data)
        } else {
            getDecryptedStringByRSA(alias, data)
        }*/
        return decryptedStringByAES(alias, data)
    }

    private fun getPublicKey(alias: String): PublicKey? {
        val keystore = KeyStore.getInstance(KEY_STORE)
                .apply { load(null) }
        if (!keystore.containsAlias(alias)) {
            return null
        }
        val cert = keystore.getCertificate(alias)
        return cert.publicKey
    }

    fun getPrivateKey(alias: String): PrivateKey? {
        val keyStore = KeyStore.getInstance(KEY_STORE)
                .apply { load(null) }
        if (!keyStore.containsAlias(alias)) {
            return null
        }
        val privateKeyEntry = keyStore.getEntry(alias, null) as KeyStore.PrivateKeyEntry
        return privateKeyEntry.privateKey
    }

    private fun encryptedStringByAES(alias: String, data: String): String? {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val publicKey = getKeyAES(alias)
        publicKey?.let {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey, GCMParameterSpec(128, FIXED_IV.toByteArray()))
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Base64.getEncoder().encodeToString(cipher.doFinal(data.toByteArray()))
            } else {
                android.util.Base64.encodeToString(cipher.doFinal(data.toByteArray()), android.util.Base64.DEFAULT)
            }
        }
        return null
    }

    private fun decryptedStringByAES(alias: String, data: String): String? {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val privateKey = getKeyAES(alias)
        try {
            privateKey?.let {
                cipher.init(Cipher.DECRYPT_MODE, privateKey, GCMParameterSpec(128, FIXED_IV.toByteArray()))
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String(cipher.doFinal(Base64.getDecoder().decode(data)))
                } else {
                    String(cipher.doFinal(android.util.Base64.decode(data, android.util.Base64.DEFAULT)))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private fun getEncryptedStringByRSA(alias: String, data: String): String? {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        val publicKey = getPublicKey(alias)
        publicKey?.let {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Base64.getEncoder().encodeToString(cipher.doFinal(data.toByteArray()))
            } else {
                android.util.Base64.encodeToString(cipher.doFinal(data.toByteArray()), android.util.Base64.DEFAULT)
            }
        }
        return null
    }

    private fun getDecryptedStringByRSA(alias: String, data: String): String? {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        val privateKey = getPrivateKey(alias)
        try {
            privateKey?.let {
                cipher.init(Cipher.DECRYPT_MODE, privateKey)
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String(cipher.doFinal(Base64.getDecoder().decode(data)))
                } else {
                    String(cipher.doFinal(android.util.Base64.decode(data, android.util.Base64.DEFAULT)))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
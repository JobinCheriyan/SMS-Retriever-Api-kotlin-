package com.example.smsapi

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Base64
import android.util.Log
import org.jetbrains.anko.toast
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class AppSignature(context: Context, packageName: String) : ContextWrapper(context) {
    val appPackageName:String=packageName

    /**
     * Get all the app signatures for the current package
     * @return
     */
    // Get all package signatures for the current package
    // For each signature create a compatible hash
    val appSignatures: ArrayList<String>
        @RequiresApi(Build.VERSION_CODES.KITKAT)
        get() {
            val appCodes = ArrayList<String>()

            try {

                val packageManager = packageManager
                val signatures = packageManager.getPackageInfo(
                    appPackageName,
                    PackageManager.GET_SIGNATURES
                ).signatures
                for (signature in signatures) {
                    val hash = hash(appPackageName, signature.toCharsString())
                    if (hash != null) {
                        appCodes.add(String.format("%s", hash))
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
              toast  ( Constants.UNABLE_TO_FIND_THE_PACKAGE)
            }

            return appCodes
        }

    companion object {
        val TAG = AppSignature::class.java.simpleName

        private val HASH_TYPE = Constants.SHA_256
        val NUM_HASHED_BYTES = 9
        val NUM_BASE64_CHAR = 11

        @RequiresApi(Build.VERSION_CODES.KITKAT)
       private fun hash(packageName: String, signature: String): String? {
            val appInfo = "$packageName $signature"
            try {
                val messageDigest = MessageDigest.getInstance(HASH_TYPE)
                messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
                var hashSignature = messageDigest.digest()

                // truncated into NUM_HASHED_BYTES
                hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES)
                // encode into Base64
                var base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
                base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)

                return base64Hash
            } catch (e: NoSuchAlgorithmException) {
                Log.e(TAG, Constants.NO_SUCH_ALGORITHM, e)
            }

            return null
        }
    }
}
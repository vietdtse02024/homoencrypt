package example.com.homomorphiccrypto.HomomotphicCrypto

import android.os.Build
import example.com.homomorphiccrypto.Extensions.MD5


class InfoDevices {
    val model = Build.MODEL
    val id = Build.ID
    val manufac = Build.MANUFACTURER
    val brand = Build.BRAND
    val type = Build.TYPE
    val user = Build.USER

    fun getInfo(): String {
        return "$model-$id-$manufac-$brand-$type-$user".MD5()
    }
}
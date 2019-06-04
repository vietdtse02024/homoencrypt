package example.com.homomorphiccrypto.Main

import android.app.Application
import example.com.homomorphiccrypto.HomomotphicCrypto.InfoDevices
import example.com.homomorphiccrypto.HomomotphicCrypto.StringCipher


class MainApplication : Application() {
    lateinit var mainCipher: StringCipher

    init {
        instance = this
    }

    companion object {
        private lateinit var instance: MainApplication

        @JvmStatic
        fun getCipher(): StringCipher = instance.mainCipher
    }

    override fun onCreate() {
        super.onCreate()
        mainCipher = StringCipher(this, InfoDevices().getInfo())
    }


}
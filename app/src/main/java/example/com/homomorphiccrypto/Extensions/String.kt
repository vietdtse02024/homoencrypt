package example.com.homomorphiccrypto.Extensions

import java.lang.NumberFormatException
import java.security.MessageDigest


fun String.parseLong() : Long{
    if(this.isEmpty()){
        return 0L
    }
    try {
        return this.toLong()
    }catch (e: NumberFormatException){
        e.printStackTrace()
    }
    return 0L
}
fun String.replaceAll() : String{
    return this.replace(",","")
}
fun String.MD5() : String{
    val md = MessageDigest.getInstance("MD5")
    val digested = md.digest(toByteArray())
    return digested.joinToString(""){
        String.format("%02x",it)
    }
}
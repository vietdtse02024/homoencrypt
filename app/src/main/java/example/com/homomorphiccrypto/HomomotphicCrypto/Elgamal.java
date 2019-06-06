package example.com.homomorphiccrypto.HomomotphicCrypto;

import java.math.BigInteger;
import java.util.Random;

public class Elgamal {
    BigInteger g = new BigInteger("6");
    int secretKey = 12;
    BigInteger h;
    public String encrypt(int value) {
        h = g.pow(secretKey);
        Random random = new Random();
        int k = random.nextInt(98) + 2;
        BigInteger x = g.pow(k);
        BigInteger y = h.pow(k).multiply(g.pow(value));
        return x + "," + y;
        //return y.divide(x.pow(secretKey));
    }

    public Double decrypt(double value) {
        return Math.log(value)/Math.log(g.doubleValue());
    }

}

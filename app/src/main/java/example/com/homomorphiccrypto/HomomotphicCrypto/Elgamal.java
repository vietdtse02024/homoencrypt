package example.com.homomorphiccrypto.HomomotphicCrypto;

import java.math.BigInteger;
import java.util.Random;

public class Elgamal {
    BigInteger g = new BigInteger("10");
    int secretKey = 12;
    BigInteger h;
    public BigInteger encrypt(int value) {
        h = g.pow(secretKey);
        Random random = new Random();
        int k = random.nextInt(98) + 2;
        BigInteger x = g.pow(k);
        BigInteger y = h.pow(k).multiply(g.pow(value));
        return y.divide(x.pow(secretKey));
    }

}

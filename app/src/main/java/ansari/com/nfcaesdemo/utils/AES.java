package ansari.com.nfcaesdemo.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] encrypt(byte[] plain, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(plain);
            return cipherText;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] decrypt(byte[] encrypted, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
//            byte[] cipherText = Base64.getDecoder().decode(encrypted);
            return cipher.doFinal(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public int test(){
        return  1;
    }
}

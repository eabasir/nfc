package ansari.com.nfcaesdemo;

import android.annotation.TargetApi;
import android.os.Build;

import java.nio.ByteBuffer;
import java.util.Arrays;

import ansari.com.nfcaesdemo.types.NFCData;
import ansari.com.nfcaesdemo.utils.AES;
import ansari.com.nfcaesdemo.utils.StringFunction;

public class Lib {

    @TargetApi(Build.VERSION_CODES.O)
    public NFCData extractNFCInfo(String input, String prefix, String _key) {

        try {


            if (input == null || input.equalsIgnoreCase("") ||
                    prefix == null || prefix.equalsIgnoreCase("") ||
                    _key.equalsIgnoreCase(""))
                throw new Exception("incomplete arguments");


            NFCData nfcData = new NFCData();

            String part = input.replace(prefix, "");


            byte[] arr_url = part.getBytes("UTF-8");

            arr_url = Arrays.copyOfRange(arr_url, 0, 32);


            String encryptedHex = "";
            for (int i = 0; i < arr_url.length; i++) {

                byte[] curByte = new byte[1];
                curByte[0] = (byte) (arr_url[i] - 0x41);


                String hex = StringFunction.getHexString(curByte);
                if (hex.substring(0, 1).equals("0"))
                    hex = hex.substring(1);

                encryptedHex += hex;

            }
            nfcData.EncryptedHEX = encryptedHex;


            byte[] key = StringFunction.hexStringToByteArray(_key);


            byte[] hash = StringFunction.hexStringToByteArray(encryptedHex);


            byte[] decrypted = AES.decrypt(hash, key);

            if (decrypted != null && decrypted.length > 0) {

                String decryptedHex = StringFunction.getHexString(decrypted);

                nfcData.DycreptedHEX = decryptedHex;

                byte[] arr_serial = Arrays.copyOfRange(decrypted, 0, 4);
                byte[] arr_count = Arrays.copyOfRange(decrypted, 10, 12);

                int serial = ByteBuffer.wrap(arr_serial).getInt();

                int count = ((arr_count[1] & 0xff) << 8) | (arr_count[0] & 0xff);

                nfcData.Serial = serial;
                nfcData.Count = count;

            }
            return  nfcData;


        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

}

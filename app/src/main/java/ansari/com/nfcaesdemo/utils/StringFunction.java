package ansari.com.nfcaesdemo.utils;

import android.util.Log;

import java.util.Arrays;

public class StringFunction {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();


    public static byte[] hexStringToByteArray(String s) throws Exception {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String getHexString(byte[] bytesData) throws Exception {
        char[] hexChars = new char[bytesData.length * 2];
        for (int j = 0; j < bytesData.length; j++) {
            int v = bytesData[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    public static byte[] makeKey(String newKey) throws Exception {

        try {

            byte[] arr_key = hexStringToByteArray(newKey);
            byte[] completeKey = new byte[20];


            byte prefix = Settings.getInstance().getKeyPrefix();
            completeKey[0] = prefix;
            System.arraycopy(arr_key, 0, completeKey, 1, arr_key.length);

            byte[] keySuffix = Settings.getInstance().getKeySuffix();

            System.arraycopy(keySuffix, 0, completeKey, 1 + arr_key.length, keySuffix.length);

            return completeKey;


        } catch (Exception e) {
            Log.e("make Key", e.getMessage());
            throw e;
        }


    }

    public static byte[] makeData(String newData) throws Exception {

        try {

            byte[] arr_key = hexStringToByteArray(newData);
            byte[] completeData = new byte[20];


            byte prefix = Settings.getInstance().getKeyPrefix();
            completeData[0] = prefix;
            System.arraycopy(arr_key, 0, completeData, 1, arr_key.length);

            byte[] dataSuffix = Settings.getInstance().getDataSuffix();
            System.arraycopy(dataSuffix, 0, completeData, 1 + arr_key.length, dataSuffix.length);

            return completeData;


        } catch (Exception e) {
            Log.e("make Key", e.getMessage());
            throw e;
        }


    }
}

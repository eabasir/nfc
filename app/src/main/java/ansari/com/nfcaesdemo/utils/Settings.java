package ansari.com.nfcaesdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ansari.com.nfcaesdemo.App;


/**
 * Created by Eabasir on 5/26/2016.
 */
public class Settings {


    private final String KEY_MAIN_PREF = "com.ansari.nfcaesdemo";

    private static Settings Instance;

    private Context mContext;

    private SharedPreferences mSharedPreferences;

    private final static int WRITE_BLOCK_NO = 0;
    private final static int READ_DATA_BLOCK_NO = 2;
    private final static int READ_DATA_BLOCK_LENGTH = 3;
    private final static byte PREFIX = (byte) 0xaa;
    private final static byte[] KEY_SUFFIX = new byte[]{(byte) 0xab, 0x00, 0x00};
    private final static byte[] DATA_SUFFIX = new byte[]{(byte) 0x74, 0x00, 0x00};


    public static Settings getInstance() {
        if (Instance == null) {
            Instance = new Settings(App.context);
        }
        return Instance;
    }

    private Settings(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(KEY_MAIN_PREF, Context.MODE_PRIVATE);

    }


    public int getWriteBlockNo() {
        return WRITE_BLOCK_NO;
    }

    public byte getKeyPrefix() {
        return PREFIX;
    }

    public byte[] getKeySuffix() {
        return KEY_SUFFIX;
    }

    public byte[] getDataSuffix() {
        return DATA_SUFFIX;
    }

    public int getReadDataBlockNo() {
        return READ_DATA_BLOCK_NO;
    }
    public int getReadDataBlockLength() {
        return READ_DATA_BLOCK_LENGTH;
    }


    public String getSavedKey() {
        return mSharedPreferences.getString("Key", "");
    }

    public void saveKey(String key) {
        mSharedPreferences.edit().putString("Key", key).apply();
    }


}

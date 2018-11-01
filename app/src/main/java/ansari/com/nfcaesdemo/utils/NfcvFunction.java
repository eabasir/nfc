package ansari.com.nfcaesdemo.utils;

import java.io.IOException;
import java.util.Arrays;

import android.nfc.FormatException;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.util.Log;

import static ansari.com.nfcaesdemo.utils.StringFunction.getHexString;


public class NfcvFunction {
    static String TAG = "NfcV";


    public static void write(Tag tag, byte[] data, int blockNum) throws Exception {

        if(data.length % 4 != 0)
            throw  new Exception("data must be multiplication of 4 bytes");


        if (tag == null) {
            return;
        }
        NfcV nfc = NfcV.get(tag);

        nfc.connect();

        Log.d(TAG, "Max Transceive Bytes: " + nfc.getMaxTransceiveLength());


        int counter = 0;

        while (counter < data.length) {

            int start = counter;
            int end = counter + 4;

            byte[] part = Arrays.copyOfRange(data, start, end);

            byte[] arrByte = new byte[7];
            // Flags:0x02 with low data rate, 0x42 with high data rate
            arrByte[0] = 0x02;
            // ISO 15693 Single Block write command byte
            arrByte[1] = 0x21;
            // block number
            arrByte[2] = (byte) (blockNum);

            // data, DONT SEND LSB FIRST!
            arrByte[3] = part[0];
            arrByte[4] = part[1];
            arrByte[5] = part[2];
            arrByte[6] = part[3];

            try {
                Log.d(TAG, "Writing Data to block " + blockNum + " ["
                        + getHexString(arrByte) + "]");
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                nfc.transceive(arrByte);
            } catch (IOException e) {
                if (e.getMessage().equals("Tag was lost.")) {
                    // continue, because of Tag bug
                    Log.d(TAG, e.toString());
                } else {
                    throw e;
                }
            }

            counter += 4;
            blockNum +=1;
        }
        nfc.close();

    }

    public static byte[] read(Tag tag, int blockNum) {
        byte[] data = null;
        if (tag != null) {
            // set up read command buffer
            byte blockNo = (byte) blockNum; // block address
            byte[] id = tag.getId();
            byte[] readCmd = new byte[3 + id.length];
            readCmd[0] = 0x20; // set "address" flag (only send command to this tag)
            readCmd[1] = 0x20; // ISO 15693 Single Block Read command byte

            System.arraycopy(id, 0, readCmd, 2, id.length); // copy ID
            readCmd[2 + id.length] = blockNo; // 1 byte payload: block address
            try {
                Log.d(TAG, "Reading block " + blockNum + ", cmd:["
                        + getHexString(readCmd) + "]");
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            NfcV tech = NfcV.get(tag);
            if (tech != null) {
                // send read command
                try {
                    tech.connect();
                    data = tech.transceive(readCmd);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        tech.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return data;
    }




}

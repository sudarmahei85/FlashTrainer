package hci.divinesymphony.net.flashtrainer.sync;

import java.io.InputStream;

/**
 * Created by rick on 4/4/15.
 */
public abstract class CheckSum {

    abstract byte[] hashByte(InputStream is);

    public final String hashString(InputStream is) {
        return byteArrayToHexString(this.hashByte(is));
    }

    protected static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i=0; i < b.length; i++) {
            result +=
                    Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

}

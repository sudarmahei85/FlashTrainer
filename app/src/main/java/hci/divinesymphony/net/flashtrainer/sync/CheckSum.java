package hci.divinesymphony.net.flashtrainer.sync;

import java.io.InputStream;

/**
 * Created by rick on 4/4/15.
 */
public abstract class CheckSum {

    abstract byte[] hashByte(InputStream is);
    abstract String hashString(InputStream is);
}
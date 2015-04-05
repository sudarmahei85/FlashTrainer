package hci.divinesymphony.net.flashtrainer.sync;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by rick on 4/4/15.
 */
public class Sha256Sum extends CheckSum {

    private final int bufferSize;

    public Sha256Sum() {
        this.bufferSize = 1024;
    }

    public byte[] hashByte(InputStream is) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("The runtime doesn't support SHA-256!", e);
        }
        byte [] buffer = new byte[this.bufferSize];
        int sizeRead = -1;
        try {
            while ((sizeRead = is.read(buffer)) != -1) {
                digest.update(buffer, 0, sizeRead);
            }
            is.close();
        } catch (IOException e) {
            throw new RuntimeException("Failure reading stream for checksum calculation", e);
        }

        byte [] hash = null;
        hash = new byte[digest.getDigestLength()];
        hash = digest.digest();
        return hash;
    }
}

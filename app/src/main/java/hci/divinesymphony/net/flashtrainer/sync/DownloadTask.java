package hci.divinesymphony.net.flashtrainer.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;

import hci.divinesymphony.net.flashtrainer.beans.DisplayItem;

/**
 * This code started from an example provided at
 * https://gafurbabu.wordpress.com/2012/02/29/download-file-in-android-by-using-asynctask-in-background-operations/
 */

public class DownloadTask extends AsyncTask<DisplayItem, Integer, Long> {
    private Context context;

    public DownloadTask(Context context) {
        this.context = context;
    }

    protected Long doInBackground(DisplayItem... resources) {
        Client client = Client.getClient();
        File mediaDir = new File(client.getMediaPath());
        //make sure directory exists
        if (!mediaDir.isDirectory()) {
            mediaDir.mkdirs();
        }
        File tmpDir = new File(client.getTmpPath());
        //make sure directory exists
        if (!tmpDir.isDirectory()) {
            tmpDir.mkdirs();
        }

        int failure = 0;
        for (DisplayItem item : resources) {
            try {
                //TODO - this is a temporary hack to confirm downloads work without access to our server
                URL url;
                if ( item.getFile().startsWith("http://") || item.getFile().startsWith("https://") ) {
                    url = new URL(item.getFile());
                } else {
                    url = new URL(Client.getClient().getBaseUrl() + item.getFile());
                }
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.setRequestProperty("Client_Key", Client.getClient().getKey());
                Log.d(this.getClass().getName(), "About to call connect on " + c.getURL());
                c.connect();
                Log.d(this.getClass().getName(), "We are past the connect call");

                int lengthOfFile = c.getContentLength();

                File outputFile = new File(tmpDir, item.getFile());
                outputFile.createNewFile();
                Log.d(this.getClass().getName(), "Created temp file for the download");
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                Log.d(this.getClass().getName(), "Starting the transfer");
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();
                Log.d(this.getClass().getName(), "Finished receiving resource");

                //verify the download
                CheckSum sum = new Sha256Sum();
                String calc = sum.hashString(new BufferedInputStream(new FileInputStream(outputFile)));
                boolean valid = calc.equals(item.getSha256());

                File newFile = new File(mediaDir, item.getFile());

                if (valid) {
                    Log.d(this.getClass().getName(), "Verifying the download");
                    outputFile.renameTo(newFile);
                    Log.d(this.getClass().getName(), "Download verified and finalized");
                } else {
                    Log.d(this.getClass().getName(), "Download failed verification");
                    outputFile.delete();
                    Log.d(this.getClass().getName(), "Temporary file deleted");
                    failure++;
                }
            } catch (IOException e) {
                Log.w(this.getClass().getName(), "Download failure", e);
                failure++;
            }

        }
        return new Long(failure);
    }

}

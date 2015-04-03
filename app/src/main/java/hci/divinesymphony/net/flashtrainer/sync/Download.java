package hci.divinesymphony.net.flashtrainer.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * This code started from an example provided at
 * https://gafurbabu.wordpress.com/2012/02/29/download-file-in-android-by-using-asynctask-in-background-operations/
 */

public class Download {
    private Context context;

    public Download(Context context) {
        this.context = context;
    }

    protected void fetch(String... resources) {
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

        for (String resource : resources) {
            try {
//TODO - make this download the supplied file instead
//                URL url = new URL(Client.getClient().getBaseUrl()+resource);
                URL url = new URL("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.setRequestProperty("Client_Key", Client.getClient().getKey());
                Log.w(this.getClass().getName(), "About to call connect");
                c.connect();
                Log.w(this.getClass().getName(), "We are past the connect call");

                int lengthOfFile = c.getContentLength();

                File outputFile = new File(tmpDir, resource);
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();

                //verify the download
                //TODO - add hash verification code before executing this next bit
                File newFile = new File(mediaDir + resource);
                outputFile.renameTo(newFile);

            } catch (IOException e) {
                Log.e(this.getClass().getName(), "Not Good");
            }

        }

    }

}

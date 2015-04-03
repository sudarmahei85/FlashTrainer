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
import java.net.URL;

/**
 * This code started from an example provided at
 * https://gafurbabu.wordpress.com/2012/02/29/download-file-in-android-by-using-asynctask-in-background-operations/
 */

/*
public class MyActivity extends Activity  {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        new Download(MyActivity.this,”Your Download File Url”).execute();

    }

}
*/

public class Download extends AsyncTask<URL, Void, String> {
    private Context context;
    private final URL url;

    public Download(Context context,URL url) {
        this.context = context;
        this.url=url;
    }

    public Download(Context context, String url) throws MalformedURLException {
        this(context, new URL(url));
    }

    protected void onPreExecute() {
    }

    protected String doInBackground(URL... urls) {

        for (URL url : urls) {
            try {
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.setRequestProperty("Client_Key", Client.getClient().getKey());
                c.connect();
                String[] path = url.getPath().split("/");
                String mp3 = path[path.length - 1];
                int lengthOfFile = c.getContentLength();

                String PATH = Environment.getExternalStorageDirectory() + "/DownLoad/";
                Log.v("", "PATH: " + PATH);
                File file = new File(PATH);
                file.mkdirs();

                String fileName = mp3;

                File outputFile = new File(file, fileName);
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {

                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "done";
    }

    protected void onPostExecute(String result) {
    }

}

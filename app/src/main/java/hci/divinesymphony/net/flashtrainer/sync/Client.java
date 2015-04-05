package hci.divinesymphony.net.flashtrainer.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.UUID;

/**
 * Created by rick on 4/3/15.
 */
public class Client {

    private static final String CLIENT_PREFS = "client";
    private static final String KEY = "client_key";
    private static final String MEDIA_BASE_URL = "mediabase_url";
    private static final String XML_BASE_URL = "xmlbase_url";

    private static final String MEDIA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AuPAIR/media/";
    private static final String TMP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AuPAIR/tmp/";

    /* The singleton */
    private static Client client;

    private final String clientKey;
    private final String mediaBaseUrl;
    private final String xmlBaseUrl;

    public static Client initialize(Context ctx) {
        if (client == null) {
            client = new Client(ctx);
        }
        return client;
    }

    public static Client getClient() {
        return client;
    }

    private Client(Context ctx) {
        // Restore preferences
        SharedPreferences settings = ctx.getSharedPreferences(CLIENT_PREFS, 0);
        SharedPreferences.Editor editor = null;

        String id = settings.getString(KEY, null);
        String mediaBaseUrl = settings.getString(MEDIA_BASE_URL, null);
        String xmlBaseUrl = settings.getString(XML_BASE_URL, null);

        if (id == null) {
            if (editor == null) {
                editor = settings.edit();
            }
            //generate new id
            id = UUID.randomUUID().toString();
            editor.putString(KEY, id);
            Log.i(this.getClass().getName(), "generated a new client key:" + id);
        }

        if (mediaBaseUrl == null) {
            if (editor == null) {
                editor = settings.edit();
            }
            //store the default
            mediaBaseUrl = "http://aupair.divinesymphony.net/media/";
//TODO once finalized, uncomment this to persist in the client
//            editor.putString(MEDIA_BASE_URL, mediaBaseUrl);
        }

        if (xmlBaseUrl == null) {
            if (editor == null) {
                editor = settings.edit();
            }
            //store the default
            xmlBaseUrl = "http://aupair.divinesymphony.net/xml/";
//TODO once finalized, uncomment this to persist in the client
//            editor.putString(XML_BASE_URL, xmlBaseUrl);
        }

        if (editor != null) {
            editor.apply();
        }

        this.clientKey = id;
        this.xmlBaseUrl = xmlBaseUrl;
        this.mediaBaseUrl = mediaBaseUrl;
    }

    /**
     * Return the media URL
     * @return the media URL
     */
    public String getMediaUrl() {
        Log.v(this.getClass().getName(), "getMediaUrl() = "+this.mediaBaseUrl);
        return this.mediaBaseUrl;
    }

    /**
     * Returns the unique client identifier
     * @return returns the unique client identifier
     * */
    public String getKey() {
        Log.v(this.getClass().getName(), "getKey() = "+this.clientKey);
        return this.clientKey;
    }

    /**
     * Returns a partial client identifier, for manual entry on the web application
     * @return a short version of the client identifier
     */
    public String getUserKey() {
//TODO - temporarily return the full key
        String str = this.getKey();
//        String str = this.clientKey.substring(0,8);
        Log.v(this.getClass().getName(), "getUserKey() = "+str);
        return str;
    }

    /**
     * Return the media path for the application
     * @return media path for the application
     */
    public String getMediaPath() {
        Log.v(this.getClass().getName(), "getMediaPath() = "+MEDIA_PATH);
        return MEDIA_PATH;
    }

    /**
     * Return the temporary file path for the application
     * @return temporary path for the application
     */
    public String getTmpPath() {
        Log.v(this.getClass().getName(), "getTmpPath() = "+TMP_PATH);
        return TMP_PATH;
    }

    /**
     * Return the XML Config URL
     * @return the XML Config URL
     */
    public String getConfigUrl() {
        Log.v(this.getClass().getName(), "getConfigUrl() = "+this.xmlBaseUrl+this.getKey());
        return this.xmlBaseUrl+this.getKey();
    }

    public InputStream getConfigXMLTemp(Context ctx) {
        return this.getConfigXML(ctx, true);
    }

    public InputStream getConfigXML(Context ctx) {
        return this.getConfigXML(ctx, false);
    }

    private InputStream getConfigXML(Context ctx, boolean useTemp) {
        InputStream is;
        File configFile;
        if (useTemp) {
            configFile = new File(this.getMediaPath() + Client.getClient().getKey());
        } else {
            configFile = new File(this.getMediaPath() + "current.xml");
            if (!configFile.exists()) {
                InputStream template;
                OutputStream out;
                try {
                    template = ctx.getAssets().open("current.xml");
                    out = new FileOutputStream(configFile);
                    byte[] buffer = new byte[1024];
                    int len = template.read(buffer);
                    while (len != -1) {
                        out.write(buffer, 0, len);
                        len = template.read(buffer);
                    }
                    template.close();
                    out.close();
                } catch (IOException e) {
                    Log.e(this.getClass().getName(), "this is really bad");
                    throw new RuntimeException("This is really bad", e);
                }
            }
        }
        try {
//            if (configFile.exists()) {
                is = new FileInputStream(configFile);
//            } else {
//                is = ctx.getAssets().open("current.xml");
//            }
        } catch (IOException e) {
            Log.e(this.getClass().getName(), "Unable to read the xml config");
            throw new RuntimeException("Unable to read the xml config", e);
        }
        return is;
    }

}

package hci.divinesymphony.net.flashtrainer.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.math.BigInteger;
import java.util.UUID;

/**
 * Created by rick on 4/3/15.
 */
public class Client {

    private static final String CLIENT_PREFS = "client";
    private static final String KEY = "client_key";
    private static final String BASE_URL = "base_url";

    private static final String MEDIA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AuPAIR/media/";
    private static final String TMP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AuPAIR/tmp/";

    /* The singleton */
    private static Client client;

    private final String clientKey;
    private final String baseUrl;


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
        String baseUrl = settings.getString(BASE_URL, null);

        if (id == null) {
            editor = settings.edit();
            //generate new id
            id = UUID.randomUUID().toString();
            editor.putString(KEY, id);
        }

        if (baseUrl == null) {
            editor = settings.edit();
            //store the default
            baseUrl = "https://aupair.divinesymphony.net/downloads/";
//TODO - this setting is temporarily disabled until we have working downloads via this address
//            editor.putString(BASE_URL, baseUrl);
        }

        if (editor != null) {
            editor.commit();
        }

        this.clientKey = id;
        this.baseUrl = baseUrl;
    }

    /**
     * Return the base URL
     * @return the base URL
     */
    public String getBaseUrl() {
        Log.v(this.getClass().getName(), "getBaseUrl() = "+this.baseUrl);
        return this.baseUrl;
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
//        String str = this.clientKey.substring(0,3)+'-'+this.clientKey.substring(4,7);
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
}

package hci.divinesymphony.net.flashtrainer.sync;

import android.content.Context;

/**
 * Created by rick on 4/3/15.
 */
public class Downloader {

    private final Download download;

    /**
     * Create a new downloader object
     * @param ctx the context of the application
     */
    public Downloader(Context ctx) {
        this.download = new Download(ctx);
    }

    /**
     * Download the rewards block from the given XML file
     */
    public void getRewards() {
        //TODO - make this read from a specified XML file instead of the hard-coded name
        String[] resources = new String[1];
        resources[0] = "delete_me";
        this.download.fetch(resources);
    }

    /**
     * Download the multimedia content, except for the rewards
     */
    public void getMedia() {
        //TODO - make this actually download the multimedia (except rewards)
    }

}

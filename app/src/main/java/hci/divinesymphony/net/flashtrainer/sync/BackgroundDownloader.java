package hci.divinesymphony.net.flashtrainer.sync;

import android.content.Context;

/**
 * Created by rick on 4/3/15.
 */
public class BackgroundDownloader {

    private final Context context;

    public BackgroundDownloader(Context ctx) {
        this.context = ctx;
    }

    public void go() {
        Downloader downloader;

        downloader = new Downloader(this.context, Client.getClient().getConfigXML(this.context));
        downloader.getConfig();

//        downloader = new Downloader(this.context, Client.getClient().getConfigXMLTemp(this.context));
//        downloader.getRewards();
//        downloader.getMedia();

        //TODO - add error checking to make sure the previous steps complete without error
        downloader.enableConfig();

    }
}

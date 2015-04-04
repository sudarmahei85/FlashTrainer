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
        Downloader downloader = new Downloader(this.context);
        downloader.getMedia();
        downloader.getRewards();
    }
}

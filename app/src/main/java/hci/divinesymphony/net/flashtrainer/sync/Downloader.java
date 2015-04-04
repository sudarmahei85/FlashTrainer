package hci.divinesymphony.net.flashtrainer.sync;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import hci.divinesymphony.net.flashtrainer.backend.Selector;
import hci.divinesymphony.net.flashtrainer.beans.DisplayItem;

/**
 * Created by rick on 4/3/15.
 */
public class Downloader {

    private final Context context;
    private final Selector selector;

    /**
     * Create a new downloader object
     * @param ctx the context of the application
     */
    public Downloader(Context ctx) {
        this.context = ctx;
        //TODO - make this download from a new, non-active copy of the XML instead
        try {
            InputStream is = this.context.getAssets().open("current.xml");
            this.selector = new Selector(is);
        } catch (IOException e) {
            Log.e(this.getClass().getName(), "Unable to read xml file");
            throw new RuntimeException("Unable to read xml file", e);
        }
    }

    /**
     * Download the rewards block from the given XML file
     */
    public void getRewards() {
        DownloadTask downloadTask = new DownloadTask(this.context);
        List<DisplayItem> list = this.selector.getRewards();
        list = pruneCached(list); //filter out the stuff currently cached
        downloadTask.execute( list.toArray(new DisplayItem[0]) );
    }

    /**
     * Download the multimedia content, except for the rewards
     */
    public void getMedia() {
        DownloadTask downloadTask = new DownloadTask(this.context);
        //TODO - make this read from a specified XML file instead of the hard-coded example
        List<DisplayItem> list = new LinkedList<DisplayItem>();
        list = pruneCached(list); //filter out the stuff currently cached
        downloadTask.execute(list.toArray(new DisplayItem[0]));
    }

    private List<DisplayItem> pruneCached(List<DisplayItem> list) {
        List<DisplayItem> newList = new LinkedList<DisplayItem>();
        File parent = new File(Client.getClient().getMediaPath());
        for (DisplayItem item : list) {
            File test = new File(parent, item.getFile());
            if ( !test.exists() ) {
                newList.add(item);
            }
        }
        return newList;
    }

}

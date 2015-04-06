package hci.divinesymphony.net.flashtrainer.beans;

import android.net.Uri;

import java.net.URI;

import hci.divinesymphony.net.flashtrainer.sync.Client;

/**
 * Created by rick on 3/8/15.
 */
public class DisplayItem {

    public enum MediaType { TEXT, IMAGE, VIDEO, SOUND, CONFIG }

    private final String text;
    private final String file;
    private final MediaType type;
    private final String id;
    private final String sha256;

    public static DisplayItem getConfig() {
        return new DisplayItem(
                MediaType.CONFIG,
                null,
                Client.getClient().getConfigUrl(),
                Client.getClient().getKey(),
                null);
    }

    public DisplayItem(String text, String id) {
        this(MediaType.TEXT, id, text, null, null);
    }

    /**
     * Creates a DisplayItem
     * @param type Which type of a resource is the display item
     * @param id the id provided from the xml file
     * @param file the file/guid from the xml file (required for non-text type)
     * @param text text description, if any (required for text type)
     */
    public DisplayItem(DisplayItem.MediaType type, String id, String text, String file, String sha256) {
        this.type = type;
        this.file = file;
        this.id = id;
        this.text = text;
        this.sha256 = sha256;
        if ((type != MediaType.TEXT && type != MediaType.CONFIG) &&
                ( this.file == null||this.file.isEmpty() || this.sha256 == null|| this.sha256.isEmpty() )) {
            throw new IllegalArgumentException("All display items but text and config must have a file and checksum present");
        }
    }

    public String getText() {
        return this.text;
    }

    public String getId() {
        return this.id;
    }

    public String getFile() { return this.file; }

    public String getUrl() {
        if (this.type != MediaType.CONFIG) {
            return Client.getClient().getMediaUrl()+this.file;
        } else {
            return this.text;
        }
    }

    public Uri getLocalUri() {
        return Uri.parse("file://"+Client.getClient().getMediaPath()+this.file);
    }

    public String getSha256() { return this.sha256; }

    public boolean isVideo() { return this.type == MediaType.VIDEO; }

    public boolean isImage() { return this.type == MediaType.IMAGE; }

}

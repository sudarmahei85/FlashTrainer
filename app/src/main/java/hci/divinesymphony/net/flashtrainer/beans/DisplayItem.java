package hci.divinesymphony.net.flashtrainer.beans;

/**
 * Created by rick on 3/8/15.
 */
public class DisplayItem {

    public enum MediaType { TEXT, IMAGE, VIDEO, SOUND }

    private final String text;
    private final String file;
    private final MediaType type;
    private final String id;

    public DisplayItem(String text, String id) {
        this(MediaType.TEXT, id, null, text);
    }

    /**
     * Creates a DisplayItem
     * @param type Which type of a resource is the display item
     * @param id the id provided from the xml file
     * @param file the file/guid from the xml file (required for non-text type)
     * @param text text description, if any (required for text type)
     */
    public DisplayItem(DisplayItem.MediaType type, String id, String file, String text) {
        this.type = type;
        this.file = file;
        this.id = id;
        this.text = text;
        if ((type != MediaType.TEXT) && ( this.file == null||this.file.isEmpty()) ) {
            throw new IllegalArgumentException("All display items but text must have a file present");
        }
    }

    public String getText() {
        return this.text;
    }

    public String getId() {
        return this.id;
    }

    public String getFile() { return this.file; }

    public boolean isVideo() { return this.type == MediaType.VIDEO; }

    public boolean isImage() { return this.type == MediaType.IMAGE; }

}

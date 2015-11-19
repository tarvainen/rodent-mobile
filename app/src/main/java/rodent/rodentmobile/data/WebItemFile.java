package rodent.rodentmobile.data;

import android.graphics.drawable.Drawable;

import rodent.rodentmobile.ui.WebItemAdapter;

/**
 * Created by Atte on 19.11.2015.
 */
public class WebItemFile {
    private Drawable thumbnail;
    private String name;

    {
        thumbnail = null;
        name = "";
    }

    public WebItemFile () {

    }

    public void setThumbnail (Drawable thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setName (String name) {
        this.name = name;
    }

    public Drawable getThumbnail () {
        return this.thumbnail;
    }

    public String getName () {
        return this.name;
    }
}

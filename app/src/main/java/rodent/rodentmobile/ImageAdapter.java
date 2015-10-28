package rodent.rodentmobile;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Teemu on 21.10.2015.
 * <br>
 * Adapter used by GridView in LibraryActivity. Currently only displays text
 * items.
 */
public class ImageAdapter extends BaseAdapter {

    private ArrayList<File> files;
    private Context context;

    public ImageAdapter(Context context, ArrayList<File> files) {
        this.context = context;
        this.files = files;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TextView is used for initial testing. The library should display file
        // thumbnails as ImageViews.
        TextView textView;
        if (convertView == null) {
            textView = new TextView(context);
            textView.setLayoutParams(new GridView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 85));
            textView.setPadding(8, 8, 8, 8);
        } else {
            textView = (TextView) convertView;
        }

        textView.setText(files.get(position).getName());
        return textView;
    }
}

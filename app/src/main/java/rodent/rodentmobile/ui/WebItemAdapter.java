package rodent.rodentmobile.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rodent.rodentmobile.R;
import rodent.rodentmobile.data.WebItemFile;

/**
 * Created by Atte on 19.11.2015.
 */
public class WebItemAdapter extends BaseAdapter {

    private List<WebItemFile> files;
    private Context context;

    public WebItemAdapter (Context context, List<WebItemFile> files) {
        this.context = context;
        this.files = files;
    }

    @Override
    public int getCount () {
        return files.size();
    }

    @Override
    public Object getItem (int position) throws IndexOutOfBoundsException {
        return files.get(position);
    }

    @Override
    public long getItemId (int position) {
        return 0;
    }


    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.webitem, null);

        ((TextView)view.findViewById(R.id.webItemName)).setText(files.get(position).getName());
        ImageView imageView = (ImageView) view.findViewById(R.id.webItemThmbnail);
        imageView.setImageDrawable(files.get(position).getThumbnail());

        if (files.get(position).getThumbnail() == null) {
            imageView.setImageResource(R.drawable.defbg);
        }

        return view;
    }
}

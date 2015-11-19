package rodent.rodentmobile.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import rodent.rodentmobile.R;
import rodent.rodentmobile.filesystem.MyFile;

/**
 * Created by Teemu on 21.10.2015.
 * <br>
 *
 */
public class ImageAdapter extends BaseAdapter {

    private ArrayList<MyFile> files;
    private Context context;

    public ImageAdapter(Context context, ArrayList<MyFile> files) {
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
        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.library_item, null);

        ((TextView)view.findViewById(R.id.txtViewFileName)).setText(files.get(position).getFilename());
        ImageView imageView = (ImageView) view.findViewById(R.id.imgViewThumbnail);
        Bitmap thumbnail = files.get(position).getBitmap();
        if (thumbnail == null) {
            imageView.setImageResource(R.drawable.defbg);
        } else {
            imageView.setImageBitmap(thumbnail);
        }


        return view;
    }
}

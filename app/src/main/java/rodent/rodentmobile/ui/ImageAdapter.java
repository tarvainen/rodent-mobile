package rodent.rodentmobile.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import rodent.rodentmobile.R;

/**
 * Created by Teemu on 21.10.2015.
 * <br>
 *
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
        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.library_item, null);

        ((TextView)view.findViewById(R.id.txtViewFileName)).setText(files.get(position).getName());
        ImageView imageView = (ImageView) view.findViewById(R.id.imgViewThumbnail);
        Bitmap bm = BitmapFactory.decodeFile(
                files.get(position) + ".png");
        imageView.setImageBitmap(bm);

        if (bm == null) {
            imageView.setImageResource(R.drawable.defbg);
        }

        return view;
    }
}

package rodent.rodentmobile;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * Created by Atte on 24.10.2015.
 */
public class IconSpinnerAdapter extends ArrayAdapter<String> {

    private TypedArray idResources;
    private TypedArray drawableResources;

    public IconSpinnerAdapter(Context context, int resource, TypedArray idResources, TypedArray drawableResources, String[] nameResources) {
        super(context, resource, nameResources);
        this.idResources = idResources;
        this.drawableResources = drawableResources;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View spinner = inflater.inflate(R.layout.icon_spinner_row, parent, false);
        ImageView button = (ImageView)spinner.findViewById(R.id.spinner_image);
        button.setImageResource(this.drawableResources.getResourceId(position, 0));
        return spinner;
    }

    public TypedArray getIdResources () {
        return this.idResources;
    }

    public TypedArray getDrawableResources () {
        return this.drawableResources;
    }
}

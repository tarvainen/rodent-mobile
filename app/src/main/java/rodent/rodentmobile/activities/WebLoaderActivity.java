package rodent.rodentmobile.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rodent.rodentmobile.R;
import rodent.rodentmobile.data.GCodeParser;
import rodent.rodentmobile.data.HttpLoader;
import rodent.rodentmobile.data.WebItemFile;
import rodent.rodentmobile.exceptions.InvalidGCodeException;
import rodent.rodentmobile.filesystem.MyFile;
import rodent.rodentmobile.filesystem.RodentFile;
import rodent.rodentmobile.ui.WebItemAdapter;

public class WebLoaderActivity extends AppCompatActivity {

    private List<WebItemFile> files;
    private GridView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_loader);
        view = (GridView) findViewById(R.id.gridView);
        updateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web_loader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateList () {
        URL uri;
        try {
            uri = new URL(getString(R.string.online_api_address));
            WebItemLoader loader = new WebItemLoader();
            loader.execute(uri);
        } catch (MalformedURLException ex) {
            Toast.makeText(this, R.string.web_error_url_syntax, Toast.LENGTH_SHORT).show();
            return;
        }
    }


    private class WebItemLoader extends AsyncTask<URL, Void, Boolean> {

        private ProgressDialog dialog = new ProgressDialog(WebLoaderActivity.this);

        private List<WebItemFile> filelist;
        private URL url;

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(getString(R.string.drawing_notification_saving));
            this.dialog.show();
        }

        @Override
        protected Boolean doInBackground(URL... urls) {
            try {
                url = urls[0];
                InputStream input = urls[0].openStream();
                filelist = toJSON(input);
            } catch (IOException ex) {
                return false;
            }

            return true;
        }

        private List<WebItemFile> toJSON (InputStream iStream) throws IOException {
            JsonReader reader = new JsonReader(new InputStreamReader(iStream, "UTF-8"));
            try {
                return readMessagesArray(reader);
            } finally {
                reader.close();
            }
        }

        public List<WebItemFile> readMessagesArray(JsonReader reader) throws IOException {
            List messages = new ArrayList();

            reader.beginArray();
            while (reader.hasNext()) {

                messages.add(readMessage(reader));
            }
            reader.endArray();
            return messages;
        }

        public WebItemFile readMessage(JsonReader reader) throws IOException {
            WebItemFile file = new WebItemFile();
            String filename = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("filename")) {
                    filename = reader.nextString();
                    filename = getOnlyNameFromFilename(filename);
                    file.setName(filename);
                } else {
                    reader.skipValue();
                }
            }

            if (filename != null) {
                file.setThumbnail(drawableFromUrl(url + filename + ".png"));
            }

            reader.endObject();
            return file;
        }

        private String getOnlyNameFromFilename (String filename) {
            return filename.replace(getString(R.string.relative_replaced_address), "");
        }

        public Drawable drawableFromUrl(String url) {
            try {
                Bitmap x;
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();

                x = BitmapFactory.decodeStream(input);
                return new BitmapDrawable(x);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                files = filelist;
                showList();
                Toast.makeText(WebLoaderActivity.this, R.string.web_synced_succesfully, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(WebLoaderActivity.this, R.string.web_sync_failed, Toast.LENGTH_SHORT).show();
            }

            if (this.dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private void showList () {
        WebItemAdapter adapter = new WebItemAdapter(this, files);
        view.setAdapter(adapter);
    }

    public void onFabClick (View v) {
        switch (v.getId()) {
            case R.id.fabLoad:
                updateList();
                break;
            default:
                break;
        }
    }

    public void onButtonClick (View v) {
        switch (v.getId()) {
            case R.id.btnLoadItem:
                RelativeLayout parent = (RelativeLayout)v.getParent().getParent();
                int position = view.getPositionForView(parent);
                WebItemFile file = (WebItemFile)view.getAdapter().getItem(position);
                downloadItemWithName(file.getName());
            default:
                break;
        }
    }

    private void downloadItemWithName (String name) {
        String url = getString(R.string.online_api_address) + name;
        new GCodeLoader().execute(name, url);
    }

    private class GCodeLoader extends AsyncTask<String, Void, Boolean> {

        private ProgressDialog dialog;
        List<String> list;
        String filename;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(WebLoaderActivity.this);
            this.dialog.setCancelable(false);
            this.dialog.setMessage(getString(R.string.drawing_notification_saving));
            this.dialog.setIndeterminate(true);
            this.dialog.show();
        }

        @Override
        public void onProgressUpdate (Void... vals) {
            super.onProgressUpdate(vals);
            this.dialog.setProgress(this.dialog.getProgress() + 1);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                list = HttpLoader.getGCodesByFilename(urls[1]);
                filename = urls[0];
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);
            if (success) {
                convertToRodentFile(filename, list);
                Toast.makeText(WebLoaderActivity.this, R.string.web_synced_succesfully, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(WebLoaderActivity.this, R.string.web_sync_failed, Toast.LENGTH_SHORT).show();
            }

            if (this.dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private void convertToRodentFile (String filename, List<String> list) {
        try {
            RodentFile file = GCodeParser.parseFileFromGCode(filename, list);
            saveFile(file);
        } catch (InvalidGCodeException ex) {
            Toast.makeText(this, R.string.web_invalid_gcode, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFile(MyFile file) {
        try {
            file.save();
        } catch (IOException ex) {
            ex.printStackTrace();
            Toast.makeText(this, R.string.web_file_save_failed, Toast.LENGTH_SHORT).show();
        }
    }

}

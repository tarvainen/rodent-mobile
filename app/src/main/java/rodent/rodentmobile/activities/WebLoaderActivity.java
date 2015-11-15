package rodent.rodentmobile.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.Url;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rodent.rodentmobile.R;
import rodent.rodentmobile.data.GCodeParser;
import rodent.rodentmobile.data.HttpLoader;
import rodent.rodentmobile.exceptions.InvalidGCodeException;
import rodent.rodentmobile.filesystem.MyFile;
import rodent.rodentmobile.filesystem.RodentFile;

public class WebLoaderActivity extends AppCompatActivity {

    private List files;
    private GridView view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_loader);

        view = (GridView) findViewById(R.id.gridView);

        updateList();
    }

    private void updateList () {
        URL uri;
        try {
            uri = new URL(getString(R.string.rodentOnlineApiAddress));
            WebLoader loader = new WebLoader();
            loader.execute(uri);
        } catch (MalformedURLException ex) {
            Toast.makeText(this, R.string.web_error_url_syntax, Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private class WebLoader extends AsyncTask<URL, Void, Boolean> {

        private ProgressDialog dialog = new ProgressDialog(WebLoaderActivity.this);

        private List filelist;

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(getString(R.string.drawing_notification_saving));
            this.dialog.show();
        }

        @Override
        protected Boolean doInBackground(URL... urls) {
            try {
                InputStream input = urls[0].openStream();
                filelist = toJSON(input);
            } catch (IOException ex) {
                return false;
            }

            return true;
        }

        private List toJSON (InputStream iStream) throws IOException {
            JsonReader reader = new JsonReader(new InputStreamReader(iStream, "UTF-8"));
            try {
                return readMessagesArray(reader);
            } finally {
                reader.close();
            }
        }

        public List readMessagesArray(JsonReader reader) throws IOException {
            List messages = new ArrayList();

            reader.beginArray();
            while (reader.hasNext()) {
                messages.add(readMessage(reader));
            }
            reader.endArray();
            return messages;
        }

        public String readMessage(JsonReader reader) throws IOException {
            String filename = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("filename")) {
                    filename = reader.nextString();
                    filename = getOnlyNameFromFilename(filename);
                } else {
                    reader.skipValue();
                }
            }

            reader.endObject();
            return filename;
        }

        private String getOnlyNameFromFilename (String filename) {
            return filename.replace("./../files/", "");
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
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.webitem, R.id.webItemName, files);
        view.setAdapter(adapter);

    }

    public void onFabClick (View v) {
        switch (v.getId()) {
            case R.id.fabLoad:
                updateList();
                break;
            case R.id.fabDownloadItem:
                RelativeLayout parent = (RelativeLayout)v.getParent().getParent().getParent();
                int position = view.getPositionForView(parent);
                String url = (String)view.getAdapter().getItem(position);
                downloadItemWithName(url);
            default:
                break;
        }
    }

    private void downloadItemWithName (String name) {
        String url = getString(R.string.rodentOnlineApiAddress) + "../files/" + name;
        new GCodeLoader().execute(name, url);
    }

    private class GCodeLoader extends AsyncTask<String, Void, Boolean> {

        private ProgressDialog dialog = new ProgressDialog(WebLoaderActivity.this);
        List<String> list;
        String filename;

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(getString(R.string.drawing_notification_saving));
            this.dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                list = HttpLoader.getGCodesByFilename(urls[1]);
                filename = urls[0];
            } catch (Exception ex) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
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

    private void convertToRodentFile (String filename, List<String> gcode) {
        try {
            RodentFile file = GCodeParser.parseFileFromGCode(filename, gcode);
            saveFile(file);
        } catch (InvalidGCodeException ex) {
            Toast.makeText(this, "Invalid gcode.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFile(MyFile file) {
        try {
            file.save();
        } catch (IOException ex) {
            ex.printStackTrace();
            Toast.makeText(this, "File save failed.", Toast.LENGTH_SHORT).show();
        }
    }

}

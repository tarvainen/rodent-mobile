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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
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
import rodent.rodentmobile.filesystem.MyFile;

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
            uri = new URL("http://46.101.165.251/rodentonline/json/");
            WebLoader loader = new WebLoader();
            loader.execute(uri);
        } catch (MalformedURLException ex) {
            Toast.makeText(this, "The API address is wrong.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(WebLoaderActivity.this, "Loaded", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(WebLoaderActivity.this, "Load failed", Toast.LENGTH_SHORT).show();
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

}

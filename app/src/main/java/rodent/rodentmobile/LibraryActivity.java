package rodent.rodentmobile;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;

import rodent.rodentmobile.filesystem.MyFile;
import rodent.rodentmobile.filesystem.RodentFile;

public class LibraryActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ImageAdapter adapter;

    private void loadFiles() {
        items = new ArrayList<>();
        File file = new File(Environment.getExternalStorageDirectory() + "/rodent");
        File fileList[] = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            items.add(fileList[i].getName());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        loadFiles();

        GridView gridView = (GridView) findViewById(R.id.gridView);
        adapter = new ImageAdapter(this, items);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String item = items.get(position);
                String filetype = item.split("\\.")[1];
                switch (filetype) {
                    case "rodent":
                        MyFile file = new RodentFile();
                        file.load(Environment.getExternalStorageDirectory() + "/rodent/" + item);
                        Intent intent = new Intent(getApplicationContext(), DrawingActivity.class);
                        intent.putExtra("FILE", file);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_library, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_new) {
            // Make new file and start a new Activity for drawing.
            Intent newDrawingIntent = new Intent(this, DrawingActivity.class);
            this.startActivity(newDrawingIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}

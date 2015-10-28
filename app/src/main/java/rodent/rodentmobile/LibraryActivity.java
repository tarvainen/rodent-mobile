package rodent.rodentmobile;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;

import rodent.rodentmobile.filesystem.MyFile;
import rodent.rodentmobile.filesystem.RodentFile;

public class LibraryActivity extends AppCompatActivity implements NewFileDialogFragment.NewFileDialogListener {

    private ArrayList<String> items;
    private ImageAdapter adapter;
    private GridView gridView;

    private void loadFiles() {
        items.clear();
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

        items = new ArrayList<>();
        loadFiles();

        gridView = (GridView) findViewById(R.id.gridView);
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
        loadFiles();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_library, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_new) {
            DialogFragment dialog = new NewFileDialogFragment();
            dialog.show(getFragmentManager(), "NewFileDialogFragment");
        } else if (id == R.id.action_open_controller) {
            Intent newControllerIntent = new Intent(this, ManualControllerActivity.class);
            this.startActivity(newControllerIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String filename) {
        Intent newDrawingIntent = new Intent(this, DrawingActivity.class);
        MyFile file = new RodentFile(filename);
        newDrawingIntent.putExtra("FILE", file);
        this.startActivity(newDrawingIntent);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}

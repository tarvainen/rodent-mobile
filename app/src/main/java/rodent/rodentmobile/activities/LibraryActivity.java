package rodent.rodentmobile.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import rodent.rodentmobile.R;
import rodent.rodentmobile.filesystem.MyFile;
import rodent.rodentmobile.filesystem.RodentFile;
import rodent.rodentmobile.ui.ImageAdapter;
import rodent.rodentmobile.ui.TextPromptDialogFragment;
import rodent.rodentmobile.ui.TextPromptDialogFragment.TextPromptDialogListener;

public class LibraryActivity extends AppCompatActivity implements TextPromptDialogListener,
                                                                  OnItemClickListener,
                                                                  FilenameFilter {
    private ArrayList<File> files;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        files = new ArrayList<>();
        loadFiles();

        GridView gridView = (GridView) findViewById(R.id.gridView);
        adapter = new ImageAdapter(this, files);
        gridView.setAdapter(adapter);
        registerForContextMenu(gridView);
        gridView.setOnItemClickListener(this);
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
            DialogFragment dialog = new TextPromptDialogFragment();
            Bundle arguments = new Bundle();
            arguments.putString("PROMPT", "Enter filename:");
            dialog.setArguments(arguments);
            dialog.show(getFragmentManager(), "TextPromptDialogFragment");
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
    public void onDialogNegativeClick(DialogFragment dialog) {}

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_library, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id = item.getItemId();
        switch (id) {
            case R.id.item_delete:
                File file = files.get(info.position);
                deleteThumbnail(file);
                file.delete();
                files.remove(info.position);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyFile rodentFile = new RodentFile(files.get(position));
        Intent intent = new Intent(getApplicationContext(), DrawingActivity.class);
        intent.putExtra("FILE", rodentFile);
        startActivity(intent);
    }

    @Override
    public boolean accept(File dir, String filename) {
        return filename.toLowerCase().endsWith(".rodent");
    }

    private void deleteThumbnail(File f) {
        try {
            File thumbnail = new File(f.getParent(), f.getName() + ".png");
            thumbnail.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFiles() {
        files.clear();
        File file = new File(Environment.getExternalStorageDirectory() + "/rodent");
        File[] allFiles = file.listFiles(this);
        for (File f : allFiles) {
            files.add(f);
        }
    }

}

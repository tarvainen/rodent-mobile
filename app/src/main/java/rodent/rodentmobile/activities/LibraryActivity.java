package rodent.rodentmobile.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import com.github.clans.fab.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import rodent.rodentmobile.R;
import rodent.rodentmobile.drawing.shapes.Paper;
import rodent.rodentmobile.filesystem.MyFile;
import rodent.rodentmobile.filesystem.RodentFile;
import rodent.rodentmobile.ui.ImageAdapter;
import rodent.rodentmobile.ui.NewFileDialog;
import rodent.rodentmobile.ui.NewFileDialog.NewFileDialogListener;

public class LibraryActivity extends AppCompatActivity implements
                                                        NewFileDialogListener,
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_library, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_open_controller) {
            Intent newControllerIntent = new Intent(this, ManualControllerActivity.class);
            this.startActivity(newControllerIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String filename, float depth, float width, float height) {
        Intent newDrawingIntent = new Intent(this, DrawingActivity.class);
        MyFile file = new RodentFile(filename);
        Paper paper = new Paper();
        paper.setWidthInMills(width);
        paper.setHeightInMills(height);
        file.setRendered(true);
        file.setPaper(paper);
        newDrawingIntent.putExtra("FILE", file);
        this.startActivity(newDrawingIntent);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {}

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_library, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
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
        MyFile rodentFile;
        try {
            rodentFile = new RodentFile(files.get(position));
        } catch (Exception ex) {
            Toast.makeText(LibraryActivity.this, R.string.lib_cannot_open_file, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getApplicationContext(), DrawingActivity.class);
        intent.putExtra("FILE", rodentFile);
        startActivity(intent);
    }

    @Override
    public boolean accept(File dir, String filename) {
        return filename.toLowerCase().endsWith(getString(R.string.default_file_type));
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
        File file = new File(Environment.getExternalStorageDirectory() + getString(R.string.default_save_directory));
        File[] allFiles = file.listFiles(this);
        for (File f : allFiles) {
            files.add(f);
        }
    }

    @Override
    public void onResume () {
        super.onResume();
        loadFiles();
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent e) {
        if (keyCode == KeyEvent.KEYCODE_BACK && e.getRepeatCount() == 0) {
            finishAffinity();
            return true;
        }
        return super.onKeyDown(keyCode, e);
    }

    public void onFabClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddDrawing:
                createNewDrawing();
                break;
            case R.id.fabSettings:
                openSettings();
                break;
            case R.id.fabCloud:
                openWebLoader();
                break;
            default:
                break;
        }
    }

    private void createNewDrawing () {
        DialogFragment dialog = new NewFileDialog();
        Bundle arguments = new Bundle();
        arguments.putString("PROMPT", "Enter filename:");
        dialog.setArguments(arguments);
        dialog.show(getFragmentManager(), "NewFileDialog");
    }

    private void openSettings () {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openWebLoader () {
        Intent intent = new Intent(this, WebLoaderActivity.class);
        startActivity(intent);
    }
}

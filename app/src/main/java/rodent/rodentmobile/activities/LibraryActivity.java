package rodent.rodentmobile.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

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
                                                        FilenameFilter {

    private ArrayList<File> files;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        files = new ArrayList<>();
        adapter = new ImageAdapter(this, files);
        loadFiles();

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_library, menu);
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
            case R.id.action_cloud:
                openWebLoader();
            default:
                break;
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
    public boolean accept(File dir, String filename) {
        return filename.toLowerCase().endsWith(getString(R.string.default_file_type));
    }

    private void loadFiles() {
        files.clear();
        File file = new File(Environment.getExternalStorageDirectory() + getString(R.string.default_save_directory));
        File[] allFiles = file.listFiles(this);

        for (File f : allFiles) {
            files.add(f);
        }

        adapter.notifyDataSetChanged();
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
            /*
            case R.id.fabEditFile:
                closeMenu(v);
                File file = files.get(getClickedFabParentPosition(v));
                openFile(file);
                break;
            case R.id.fabRemoveFile:
                closeMenu(v);
                int position = getClickedFabParentPosition(v);
                File removeFile = files.get(position);
                files.remove(position);
                removeFile(removeFile);
                break;
                */
            default:
                break;
        }
    }

    public void showItemMenu (final View v) {
        PopupMenu menu = new PopupMenu(this, v);
        menu.inflate(R.menu.library_item_menu);
        menu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick (MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btnOpenItem:
                        File file = files.get(getClickedFabParentPosition(v));
                        openFile(file);
                        return true;
                    case R.id.btnDeleteItem:
                        int position = getClickedFabParentPosition(v);
                        File removeFile = files.get(position);
                        files.remove(position);
                        removeFile(removeFile);
                        return true;
                    default:
                        return false;
                }
            }
        });

        menu.show();
    }


    private int getClickedFabParentPosition (View v) {
        RelativeLayout parent = (RelativeLayout)v.getParent().getParent();
        GridView gridView = (GridView) findViewById(R.id.gridView);
        int position = gridView.getPositionForView(parent);
        return position;
    }

    private void openFile (File file) {
        MyFile rodentFile;
        try {
            rodentFile = new RodentFile(file);
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(LibraryActivity.this, R.string.lib_cannot_open_file, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getApplicationContext(), DrawingActivity.class);
        intent.putExtra("FILE", rodentFile);
        startActivity(intent);
    }

    private void removeFile(File file) {
        deleteThumbnail(file);
        file.delete();
        adapter.notifyDataSetChanged();
    }

    private void deleteThumbnail(File f) {
        try {
            File thumbnail = new File(f.getParent(), f.getName() + ".png");
            thumbnail.delete();
        } catch (Exception e) {
            e.printStackTrace();
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

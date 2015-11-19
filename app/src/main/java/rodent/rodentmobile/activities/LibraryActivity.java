package rodent.rodentmobile.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;

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

    private ArrayList<MyFile> files;
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddDrawing);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewDrawing();
            }
        });
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
                openSettings();
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
            try {
                files.add(new RodentFile(f));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume () {
        super.onResume();
        loadFiles();
    }

    public void showItemMenu (final View v) {
        PopupMenu menu = new PopupMenu(this, v);
        menu.inflate(R.menu.library_item_menu);
        menu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick (MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btnOpenItem:
                        openFile(files.get(getClickedFabParentPosition(v)));
                        return true;
                    case R.id.btnDeleteItem:
                        int position = getClickedFabParentPosition(v);
                        removeFile(files.get(position).getPath());
                        files.remove(position);
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
        return gridView.getPositionForView(parent);
    }

    private void openFile (MyFile file) {
        Intent intent = new Intent(getApplicationContext(), DrawingActivity.class);
        intent.putExtra("FILE", file);
        startActivity(intent);
    }

    private void removeFile(String path) {
        File file = new File(path);
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

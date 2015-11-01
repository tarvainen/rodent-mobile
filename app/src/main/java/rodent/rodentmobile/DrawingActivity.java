package rodent.rodentmobile;


import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import rodent.rodentmobile.actions.CopyAction;
import rodent.rodentmobile.actions.DeleteAction;
import rodent.rodentmobile.filesystem.MyFile;


public class DrawingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener, Runnable {

    private GestureDetectingDrawingBoard drawingBoard;
    private Spinner activeSpinner;

    private Handler longTouchHandler;

    private MyFile file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeNoActionBar);
        setContentView(R.layout.activity_drawing);
        this.drawingBoard = (GestureDetectingDrawingBoard) findViewById(R.id.drawing_board);
        this.createLongTouchHandler();
        this.setUpSpinners();

        setupFromFile();
    }

    private void setupFromFile() {
        try {
            file = (MyFile) getIntent().getExtras().get("FILE");
            if (file != null && file.getShapes() != null) {
                for (Shape s : file.getShapes()) {
                    drawingBoard.addDrawableElement(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        activeSpinner.performClick();
    }

    public void setUpSpinners () {
        Spinner interpolationSpinner = (Spinner) findViewById(R.id.spinner_interpolation);
        interpolationSpinner.setAdapter(createAdapter(this, R.layout.icon_spinner_row, R.array.interpolation_tool_id, R.array.interpolation_tools, R.array.interpolation_tool_names));
        interpolationSpinner.setOnItemSelectedListener(this);
        interpolationSpinner.setOnTouchListener(this);

        Spinner lineSpinner = (Spinner) findViewById(R.id.spinner_lines);
        lineSpinner.setAdapter(createAdapter(this, R.layout.icon_spinner_row, R.array.line_tool_id, R.array.line_tools, R.array.line_tool_names));
        lineSpinner.setOnItemSelectedListener(this);
        lineSpinner.setOnTouchListener(this);

        Spinner polySpinner = (Spinner) findViewById(R.id.spinner_polygons);
        polySpinner.setAdapter(createAdapter(this, R.layout.icon_spinner_row, R.array.poly_tool_id, R.array.poly_tools, R.array.poly_tool_names));
        polySpinner.setOnItemSelectedListener(this);
        polySpinner.setOnTouchListener(this);

        this.setActiveSpinner(interpolationSpinner);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long idval) {
        selectToolFromSpinnerPosition(position);
    }

    @Override
    public boolean onTouch (View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            longTouchHandler.postDelayed(this, 100);
            Spinner spinner = (Spinner) findViewById(v.getId());
            this.setActiveSpinner(spinner);
            selectToolFromSpinnerPosition(spinner.getSelectedItemPosition());
        }
        if (e.getAction() == MotionEvent.ACTION_UP) {
            this.longTouchHandler.removeCallbacks(this);
        }

        return true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void selectToolFromSpinnerPosition (int position) {
        IconSpinnerAdapter adapter = (IconSpinnerAdapter)activeSpinner.getAdapter();
        int id = adapter.getIdResources().getResourceId(position, 0);
        Tool tool = drawingBoard.getTool();
        if (tool != null) {
            tool.onDeactivation();
        }
        switch (id) {
            case R.id.tool_move:
                drawingBoard.changeTool(new MoveTool(true));
                break;
            case R.id.tool_anchor:
                drawingBoard.changeTool(new AnchorPointTool());
                break;
            case R.id.tool_rectangle:
                drawingBoard.changeTool(new RectangleTool());
                break;
            case R.id.tool_freehand:
                drawingBoard.changeTool(new FreeHandTool());
                break;
            case R.id.tool_line:
                drawingBoard.changeTool(new LineTool());
                break;
            case R.id.tool_polyline:
                drawingBoard.changeTool(new PolyLineTool());
                break;
            case R.id.tool_polygon:
                drawingBoard.changeTool(new PolygonTool());
                break;
            case R.id.tool_scale:
                drawingBoard.changeTool(new ScaleTool());
                break;
        }
    }

    public void createLongTouchHandler () {
        this.longTouchHandler = new Handler();
    }

    public void onActionButtonClick (View v) {
        switch (v.getId()) {
            case R.id.action_open_broadcast:
                openBroadcast();
                break;
            case R.id.action_delete_element:
                this.performDeleteAction();
                break;
            case R.id.action_copy_element:
                this.performCopyAction();
                break;
            case R.id.action_undo:
                break;
            case R.id.action_redo:
                break;
            case R.id.action_save_file:
                saveFile();
            default:
                break;
        }
    }

    public IconSpinnerAdapter createAdapter (Context context, int resource, int idResource, int drawableResource, int nameResource) {
        IconSpinnerAdapter adapter;
        TypedArray ids = getResources().obtainTypedArray(idResource);
        TypedArray drawables = getResources().obtainTypedArray(drawableResource);
        String names[] = getResources().getStringArray(nameResource);
        adapter = new IconSpinnerAdapter(context, resource, ids, drawables, names);
        return adapter;
    }

    public void setActiveSpinner (Spinner spinner) {
        if (this.activeSpinner != null) {
            this.activeSpinner.setBackgroundColor(getResources().getColor(R.color.button_unselected_bg));
        }
        this.activeSpinner = spinner;
        this.activeSpinner.setBackgroundColor(getResources().getColor(R.color.button_selected_bg));
    }

    private void saveThumbnail() {
        Bitmap bitmap = drawingBoard.getBitmap();
        FileOutputStream out;
        try {
            File f = new File(Environment.getExternalStorageDirectory() + "/rodent", this.file.getFilename() + ".png");
            out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFile() {
        saveThumbnail();
        file.setPaper(this.drawingBoard.getPaper());
        file.setShapes(drawingBoard.getDrawableElements());
        file.save();
        Toast.makeText(DrawingActivity.this, "Saved", Toast.LENGTH_SHORT).show();
    }

    private void openBroadcast () {
        Intent intent = new Intent(this, BroadcastActivity.class);
        MyFile file = this.file;
        intent.putExtra("FILE", file);
        this.startActivity(intent);
    }

    private void performCopyAction () {
        CopyAction copyAction = new CopyAction(drawingBoard.getDrawableElements());
        try {
            copyAction.execute();
        } catch (Exception ex) {
            Toast.makeText(this, "Copy action failed", Toast.LENGTH_SHORT).show();
        }
        drawingBoard.invalidate();
    }

    private void performDeleteAction () {
        DeleteAction action = new DeleteAction();
        action.setShapes(drawingBoard.getDrawableElements());
        if (action.execute() == 0) {
            Toast.makeText(this, "You must select elements first", Toast.LENGTH_SHORT).show();
        }
        drawingBoard.invalidate();
    }

}

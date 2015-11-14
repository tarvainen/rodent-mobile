package rodent.rodentmobile.activities;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import rodent.rodentmobile.drawing.actions.HorizontalFlipAction;
import rodent.rodentmobile.drawing.actions.VerticalFlipAction;
import rodent.rodentmobile.drawing.helpers.GestureDetectingDrawingBoard;
import rodent.rodentmobile.drawing.tools.CircleTool;
import rodent.rodentmobile.drawing.tools.RotateTool;
import rodent.rodentmobile.filesystem.RodentFile;
import rodent.rodentmobile.ui.IconSpinnerAdapter;
import rodent.rodentmobile.R;
import rodent.rodentmobile.drawing.actions.CopyAction;
import rodent.rodentmobile.drawing.actions.DeleteAction;
import rodent.rodentmobile.drawing.shapes.Shape;
import rodent.rodentmobile.drawing.tools.AnchorPointTool;
import rodent.rodentmobile.drawing.tools.FreeHandTool;
import rodent.rodentmobile.drawing.tools.LineTool;
import rodent.rodentmobile.drawing.tools.MoveTool;
import rodent.rodentmobile.drawing.tools.PolyLineTool;
import rodent.rodentmobile.drawing.tools.PolygonTool;
import rodent.rodentmobile.drawing.tools.RectangleTool;
import rodent.rodentmobile.drawing.tools.ScaleTool;
import rodent.rodentmobile.drawing.tools.Tool;
import rodent.rodentmobile.filesystem.MyFile;


public class DrawingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener, Runnable {

    private final float MAX_DEPTH = 1.0f;
    private final float MIN_DEPTH = 0.0f;
    private final int DEPTH_SELECTOR_STEPS = 10;

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
            if (file.getPaper() == null) {
                Log.d("joo", "null");
            }
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
    public void run () {
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

        Spinner circSpinner = (Spinner) findViewById(R.id.spinner_circles);
        circSpinner.setAdapter(createAdapter(this, R.layout.icon_spinner_row, R.array.circ_tool_id, R.array.circ_tools, R.array.circ_tools_names));
        circSpinner.setOnItemSelectedListener(this);
        circSpinner.setOnTouchListener(this);

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
            case R.id.tool_rotate:
                drawingBoard.changeTool(new RotateTool());
                break;
            case R.id.tool_circle:
                drawingBoard.changeTool(new CircleTool());
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
                break;
            case R.id.action_flip_horizontally:
                this.performHorizontalFlipAction();
                break;
            case R.id.action_flip_vertically:
                this.performVerticalFlipAction();
                break;
            case R.id.action_depth:
                createDepthSelector();
                break;
            default:
                break;
        }
    }

    private void createDepthSelector() {
        float depth = 0;
        for (Shape s : drawingBoard.getDrawableElements())
            if (s.isSelected())
                if (s.getDepth() > depth)
                    depth = s.getDepth();

        View view = getLayoutInflater().inflate(R.layout.depth_selector, null);
        final SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setProgress((int) (depth * DEPTH_SELECTOR_STEPS));
        final EditText editText = (EditText) view.findViewById(R.id.depth_edittext);
        editText.setText(Float.toString(depth));
        final TextView textView = (TextView) view.findViewById(R.id.depth_view);
        textView.setText(depth + "mm");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BigDecimal d = new BigDecimal(Float.toString(progress * (MAX_DEPTH / DEPTH_SELECTOR_STEPS)));
                d = d.setScale(1, BigDecimal.ROUND_HALF_DOWN);
                textView.setText(d.floatValue() + "mm");
                editText.setText(Float.toString(d.floatValue()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(view).setTitle("Shape depth")
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (Shape s : drawingBoard.getDrawableElements()) {
                    float d = Float.valueOf(editText.getText().toString());
                    if (d < MIN_DEPTH) d = MIN_DEPTH;
                    else if (d > MAX_DEPTH) d = MAX_DEPTH;
                    if (s.isSelected())
                        s.setDepth(d);
                }
            }
        }).setNegativeButton("Cancel", null);
        AlertDialog savePrompt = builder.create();
        savePrompt.show();
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
        file.setPaper(drawingBoard.getPaper());
        file.setShapes(drawingBoard.getDrawableElements());

        saveThumbnail();

        new FileAsyncSaver().execute(file);
    }

    private class FileAsyncSaver extends AsyncTask<MyFile, Void, Boolean> {

        private ProgressDialog dialog = new ProgressDialog(DrawingActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Saving. Please wait.");
            this.dialog.show();
        }

        @Override
        protected Boolean doInBackground(MyFile... files) {
            try {
                files[0].save();
            } catch (IOException ex) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) {
                dialog.dismiss();
            }

            if (success) {
                Toast.makeText(DrawingActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                drawingBoard.setModified(false);
            } else {
                Toast.makeText(DrawingActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
            }

        }

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

    private void performHorizontalFlipAction () {
        HorizontalFlipAction action = new HorizontalFlipAction();
        action.setShapes(drawingBoard.getDrawableElements());

        if (action.execute() == 0) {
            Toast.makeText(this, "You must select elements first", Toast.LENGTH_SHORT).show();
        }

        drawingBoard.invalidate();
    }

    private void performVerticalFlipAction () {
        VerticalFlipAction action = new VerticalFlipAction();
        action.setShapes(drawingBoard.getDrawableElements());

        if (action.execute() == 0) {
            Toast.makeText(this, "You must select elements first", Toast.LENGTH_SHORT).show();
        }

        drawingBoard.invalidate();
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawingBoard.isModified()) {
                createSavePromptDialog();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private void createSavePromptDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save changes?");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveFile();
                finish();
            }
        });

        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });

        AlertDialog savePrompt = builder.create();
        savePrompt.show();
    }
}

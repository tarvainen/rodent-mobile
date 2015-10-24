package rodent.rodentmobile;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;


public class DrawingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener, Runnable {

    private GestureDetectingDrawingBoard drawingBoard;
    private Spinner activeSpinner;

    private Handler longTouchHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeNoActionBar);
        setContentView(R.layout.activity_drawing);
        this.drawingBoard = (GestureDetectingDrawingBoard) findViewById(R.id.drawing_board);
        this.createLongTouchHandler();
        this.setUpSpinners();
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
        switch (id) {
            case R.id.tool_move:
                drawingBoard.changeTool(new MoveTool(true));
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
        }
    }

    public void createLongTouchHandler () {
        this.longTouchHandler = new Handler();
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

}

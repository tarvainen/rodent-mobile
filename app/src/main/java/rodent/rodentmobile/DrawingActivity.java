package rodent.rodentmobile;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DrawingActivity extends AppCompatActivity {

    private GestureDetectingDrawingBoard drawingBoard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeNoActionBar);
        setContentView(R.layout.activity_drawing);

        this.drawingBoard = (GestureDetectingDrawingBoard) findViewById(R.id.drawing_board);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drawing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonClicked(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_line:
                drawingBoard.changeTool(new LineTool());
                break;
            case R.id.button_rectangle:
                drawingBoard.changeTool(new RectangleTool());
                break;
            case R.id.button_freehand:
                drawingBoard.changeTool(new FreeHandTool());
                break;
        }
    }
}

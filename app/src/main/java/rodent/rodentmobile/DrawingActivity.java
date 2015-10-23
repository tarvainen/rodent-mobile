package rodent.rodentmobile;


import android.gesture.Gesture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class DrawingActivity extends AppCompatActivity {

    private GestureDetectingDrawingBoard drawingBoard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeNoActionBar);
        setContentView(R.layout.activity_drawing);

        this.drawingBoard = (GestureDetectingDrawingBoard) findViewById(R.id.drawing_board);

        Toolbar toolbar = (Toolbar) findViewById(R.id.drawing_toolbar_tools);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("Drawing");

        Toolbar toolbar2 = (Toolbar) findViewById(R.id.drawing_toolbar_main);
        setSupportActionBar(toolbar2);
        this.getSupportActionBar().setTitle("Tools");
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
}

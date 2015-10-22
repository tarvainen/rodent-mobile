package rodent.rodentmobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class DrawingActivity extends Activity {

    private GestureDetectingDrawingBoard drawingBoard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.drawingBoard = new GestureDetectingDrawingBoard(this);
        setContentView(this.drawingBoard);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.drawingBoard.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.drawingBoard.pause();
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

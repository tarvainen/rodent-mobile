package rodent.rodentmobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import rodent.rodentmobile.filesystem.MyFile;

public class BroadcastActivity extends AppCompatActivity {

    private MyFile file;
    private String gcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeNoActionBar);
        setContentView(R.layout.activity_broadcast);

        this.gcode = "";

        Bundle b = getIntent().getExtras();
        if (b != null) {
            try {
                file = (MyFile) getIntent().getExtras().get("FILE");
                if (file != null && file.getShapes() != null) {
                    createSimpleTestGCode();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_broadcast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createSimpleTestGCode () {
        for (Shape shape : file.getShapes()) {
            this.gcode += shape.toGCode();
        }

        Log.d("GCODE", this.gcode);
    }
}

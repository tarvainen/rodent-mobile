package rodent.rodentmobile.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import rodent.rodentmobile.R;
import rodent.rodentmobile.data.GCode;
import rodent.rodentmobile.data.GCodePackage;
import rodent.rodentmobile.data.GCodeParser;
import rodent.rodentmobile.exceptions.InvalidGCodeException;
import rodent.rodentmobile.filesystem.MyFile;
import rodent.rodentmobile.drawing.shapes.Shape;

public class BroadcastActivity extends AppCompatActivity {

    private MyFile file;

    private GCodePackage pack;

    private int currentLine;
    private boolean running;

    private WebSocketClient socket;

    private URI uri;

    {
        pack = new GCodePackage();
        currentLine = 0;
        running = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeNoActionBar);
        setContentView(R.layout.activity_broadcast);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            try {
                file = (MyFile) getIntent().getExtras().get("FILE");
                if (file != null && file.getShapes() != null) {
                    this.pack = GCodeParser.parseFileToGCode(this, file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        setUpFileData();
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        this.disconnect();
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

    public void buttonClick (View v) {
        switch (v.getId()) {
            case R.id.btn_connect:
                connectSocket();
                break;
            case R.id.btn_start_broadcast:
                running = true;
                sendMessageToSocket(getResources().getString(R.string.ask_if_ready));
                break;
            case R.id.btn_stop_broadcast:
                running = false;
                break;
            case R.id.btn_clear_coordinates:
                sendMessageToSocket(getResources().getString(R.string.clear_coordinates));
                break;
            default:
                break;
        }
    }

    private void setUpFileData () {
        if (isFileValid()) {
            setFileNameLabelValue(file.getFilename());
            setLineAmountValue();
            setMaxValues();
            setMinValues();
        }
    }

    private boolean isFileValid () {
        return this.file != null;
    }

    private void setFileNameLabelValue (String value) {
        TextView view = (TextView)findViewById(R.id.lbl_filename);
        view.setText(value);
    }

    private void setLineAmountValue () {
        TextView view = (TextView) findViewById(R.id.lbl_line_amount);
        view.setText(this.pack.getLines().length + "");
    }

    private void setMaxValues () {
        TextView view = (TextView) findViewById(R.id.lbl_max_values);
        float x = this.pack.getXValues().getY();
        float y = this.pack.getYValues().getY();
        float z = this.pack.getZValues().getY();
        String text = String.format(getString(R.string.bound_value_placeholder), x, y, z);
        view.setText(text);
    }

    private void setMinValues () {
        TextView view = (TextView) findViewById(R.id.lbl_min_values);
        float x = this.pack.getXValues().getX();
        float y = this.pack.getYValues().getX();
        float z = this.pack.getZValues().getX();
        String text = String.format(getString(R.string.bound_value_placeholder), x, y, z);
        view.setText(text);
    }

    private void connectSocket () {

        try {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            String ip = pref.getString("ip_address", "");
            this.uri = new URI("ws://" + ip + ":5000");
        } catch (URISyntaxException ex) {
            Toast.makeText(this, "Invalid socket uri", Toast.LENGTH_SHORT).show();
            this.uri = null;
        }


        if (isSocketConnected()) {
            this.disconnect();
        } else {
            if (this.uri != null) {
                this.socket = new WebSocketClient(this.uri) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        connectionOpened();
                    }

                    @Override
                    public void onMessage(String message) {
                        handleSocketMessage(message);
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        disconnected();
                    }

                    @Override
                    public void onError(Exception ex) {

                    }
                };

                this.socket.connect();
            }
        }
    }


    private boolean isSocketValid () {
        return this.socket != null;
    }

    private boolean isSocketConnected () {
        return isSocketValid() && this.socket.getConnection().isOpen();
    }

    private void connectionOpened () {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }

    private void disconnect () {
        if (isSocketConnected()) {
            this.socket.getConnection().closeConnection(0, "Closed");
        }
    }

    private void disconnected () {
        Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
    }

    private void handleSocketMessage (String message) {
        Log.d(message, message);
        try {
            GCode code = GCodeParser.parseGCodeFromRow(message);
            switch ((int)code.get("R")) {
                case 4:
                    float x = code.getX();
                    float y = code.getY();
                    float z = code.getZ();
                    setCoordinateValues(x, y, z);
                    break;
                case 10:
                    if (isRunning()) {
                        sendLine();
                    }
                    break;
                default:
                    break;
            }
        } catch (InvalidGCodeException ex) {

        }
    }

    private void setCoordinateValues (final float x, final float y, final float z) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView xLabel = (TextView) findViewById(R.id.lbl_xCoord);
                TextView yLabel = (TextView) findViewById(R.id.lbl_yCoord);
                TextView zLabel = (TextView) findViewById(R.id.lbl_zCoord);

                xLabel.setText("X" + x);
                yLabel.setText("Y" + y);
                zLabel.setText("Z" + z);
            }

        });
    }

    private boolean isRunning () {
        return this.running;
    }

    private void sendLine () {
        sendMessageToSocket(this.pack.getLines()[currentLine] + " Q0");
        if (currentLine < this.pack.getLines().length) {
            currentLine++;
        } else {
            currentLine = 0;
            running = false;
        }
    }

    private void sendMessageToSocket (String message) {
        if (isSocketConnected() && message.length() > 1) {
            this.socket.send(message);
        }
    }
}

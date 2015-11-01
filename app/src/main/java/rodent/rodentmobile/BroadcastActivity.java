package rodent.rodentmobile;

import android.app.Activity;
import android.content.Context;
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
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import rodent.rodentmobile.filesystem.MyFile;

public class BroadcastActivity extends AppCompatActivity {

    private MyFile file;

    private String gcode;
    private ArrayList<String> lines;
    private int currentLine;
    private boolean running;

    private WebSocketClient socket;

    private URI uri;

    {
        try {
            this.uri = new URI("ws://192.168.1.8:5000");
        } catch (URISyntaxException ex) {
            Log.d("Socket uri error", ex.getMessage());
            Toast.makeText(this, "Invalid socket uri", Toast.LENGTH_SHORT).show();
            this.uri = null;
        }

        lines = new ArrayList<>();
        currentLine = 0;
        running = false;
        gcode = "";
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
                    createSimpleTestGCode();
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
                sendMessageToSocket("R9");
                break;
            case R.id.btn_stop_broadcast:
                running = false;
                break;
            case R.id.btn_clear_coordinates:
                sendMessageToSocket("M28");
                break;
            default:
                break;
        }
    }

    private void createSimpleTestGCode () {
        for (Shape shape : file.getShapes()) {
            this.gcode += shape.toGCode(file.getMillInPx());
        }

        String rows[]= this.gcode.split("\n");
        for (String line : rows) {
            Log.d("i", line);
            this.lines.add(line);
        }
    }

    private void setUpFileData () {
        if (isFileValid()) {
            setFileNameLabelValue(file.getFilename());
            setLineAmountValue();
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
        view.setText(this.lines.size() + "");
    }

    private void setMaxValues () {

    }

    private void setMinValues () {

    }

    private void connectSocket () {
        Log.d("joo", "juu");
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
        int rValue = (int)getValueFromGCodeString("R", message);
        switch (rValue) {
            case 4:
                float x = getValueFromGCodeString("X", message);
                float y = getValueFromGCodeString("Y", message);
                float z = getValueFromGCodeString("Z", message);
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
    }

    private float getValueFromGCodeString (String value, String message) {
        String codes[] = message.split(" ");
        for (String code : codes) {
            if (code.startsWith(value)) {
                return parseValueFromCode(code.substring(value.length(), code.length()));
            }
        }
        return -1f;
    }

    private float parseValueFromCode (String code) {
        float result = -1f;
        try {
            result = Float.parseFloat(code);
        } catch (Exception ex) {
            Log.d("Parse error", ex.getMessage());
        }

        return result;
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
        sendMessageToSocket(this.lines.get(currentLine) + " Q0");
        if (currentLine < this.lines.size()) {
            currentLine++;
        } else {
            currentLine = 0;
            running = false;
        }
    }

    private void sendMessageToSocket (String message) {
        if (isSocketConnected() && message.length() > 1) {
            Log.d("Sending message", message);
            this.socket.send(message);
        }
    }
}

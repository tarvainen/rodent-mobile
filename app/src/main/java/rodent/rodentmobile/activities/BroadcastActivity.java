package rodent.rodentmobile.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;
import rodent.rodentmobile.R;
import rodent.rodentmobile.data.GCode;
import rodent.rodentmobile.data.GCodePackage;
import rodent.rodentmobile.data.GCodeParser;
import rodent.rodentmobile.exceptions.InvalidGCodeException;
import rodent.rodentmobile.filesystem.MyFile;

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
            openSettings();
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
            setCoordinateValues(0f, 0f, 0f);
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
        view.setText(String.format(getString(R.string.line_amount_placeholder), this.pack.getLines().length));
    }

    private void setMaxValues () {
        TextView view = (TextView) findViewById(R.id.lbl_max_values);
        float x = this.pack.getXValues().getY();
        float y = this.pack.getYValues().getY();
        float z = this.pack.getZValues().getY();
        String text = String.format(getString(R.string.bound_min_value_placeholder), x, y, z);
        view.setText(text);
    }

    private void setMinValues () {
        TextView view = (TextView) findViewById(R.id.lbl_min_values);
        float x = this.pack.getXValues().getX();
        float y = this.pack.getYValues().getX();
        float z = this.pack.getZValues().getX();
        String text = String.format(getString(R.string.bound_min_value_placeholder), x, y, z);
        view.setText(text);
    }

    private void connectSocket () {

        try {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            String ip = pref.getString("ip_address", "");
            String port = pref.getString("port", "");
            this.uri = new URI("ws://" + ip + ":" + port);
        } catch (URISyntaxException ex) {
            Toast.makeText(this, getString(R.string.brdcast_invalid_uri), Toast.LENGTH_SHORT).show();
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
                        onSocketError();
                    }
                };

                Thread t = new Thread() {
                    @Override
                    public void run () {
                        try {
                            socket.connectBlocking();
                        } catch (Exception ex) {
                            onSocketError();
                        }
                    }
                };

                t.start();
            }
        }
    }

    private boolean isSocketValid () {
        return this.socket != null;
    }

    private boolean isSocketConnected () {
        return isSocketValid() && this.socket.getConnection().isOpen();
    }

    public void connectionOpened () {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ActionMenuItemView v = (ActionMenuItemView) findViewById(R.id.icon_connection_status);
                v.setIcon(getResources().getDrawable(R.drawable.ic_signal_wifi_4_bar_white_24dp));
                Toast.makeText(BroadcastActivity.this, getString(R.string.brdcast_connection_opened), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void disconnect () {
        if (isSocketConnected()) {
            this.socket.getConnection().closeConnection(0, "Closed");
        }
    }

    private void disconnected () {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ActionMenuItemView v = (ActionMenuItemView) findViewById(R.id.icon_connection_status);
                v.setIcon(getResources().getDrawable(R.drawable.ic_signal_wifi_off_white_24dp));
                Toast.makeText(BroadcastActivity.this, getString(R.string.brdcast_connection_closed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSocketError () {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BroadcastActivity.this, getString(R.string.brdcast_connection_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSocketMessage (String message) {
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

                xLabel.setText(String.format(getString(R.string.current_x_placeholder), x));
                yLabel.setText(String.format(getString(R.string.current_y_placeholder), y));
                zLabel.setText(String.format(getString(R.string.current_z_placeholder), z));
            }
        });
    }

    private boolean isRunning () {
        return this.running;
    }

    private void sendLine () {
        if (currentLine < this.pack.getLines().length) {
            currentLine++;
        } else {
            currentLine = 0;
            running = false;
            updateProgressBar();
            return;
        }

        sendMessageToSocket(this.pack.getLines()[currentLine - 1] + " Q0");
        updateProgressBar();
    }

    private void updateProgressBar () {
        ProgressBar bar = (ProgressBar) findViewById(R.id.progBar_broadcast_status);
        float progress = this.currentLine / (float)this.pack.getLines().length;
        progress *= 100;
        bar.setProgress((int) progress);
    }

    private void sendMessageToSocket (String message) {
        if (isSocketConnected() && message.length() > 1) {
            this.socket.send(message);
        }
    }

    private void openSettings () {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}

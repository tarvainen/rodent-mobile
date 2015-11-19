package rodent.rodentmobile.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.SeekBar;
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
import rodent.rodentmobile.drawing.shapes.Shape;
import rodent.rodentmobile.exceptions.InvalidGCodeException;
import rodent.rodentmobile.filesystem.MyFile;
import rodent.rodentmobile.utilities.Utils;

public class BroadcastActivity extends AppCompatActivity {

    private MyFile file;

    private GCodePackage pack;

    private int currentLine;
    private boolean running;

    private int spindleSpeed;
    private int redLightChannel;
    private int greenLightChannel;
    private int blueLightChannel;

    private WebSocketClient socket;

    private URI uri;

    {
        pack = new GCodePackage();
        currentLine = 0;
        running = false;

        spindleSpeed = 0;
        redLightChannel = 0;
        greenLightChannel = 0;
        blueLightChannel = 0;
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
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                openSettings();
                break;
            case R.id.action_controller:
                openController();
                break;
            default:
                break;
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
                case 5:
                    Log.d("light", "reported");
                    redLightChannel = (int)code.get("r");
                    greenLightChannel = (int)code.get("g");
                    blueLightChannel = (int)code.get("b");
                    break;
                case 8:
                    spindleSpeed = (int)code.get("Q");
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

    private void openController () {

        if (!isSocketConnected()) {
            Toast.makeText(this, R.string.error_socket_not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        View view = getLayoutInflater().inflate(R.layout.machine_controller_dialog, null);
        setSpindleSpeedControllingSeekBar(view);
        setRedChannelSeekBar(view);
        setGreenChannelSeekBar(view);
        setBlueChannelSeekBar(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle(R.string.ctrl_dialog_title);
        builder.setCancelable(false);
        builder.setNegativeButton("Back", null);

        AlertDialog savePrompt = builder.create();
        savePrompt.show();
    }

    private void setSpindleSpeedControllingSeekBar (View v) {
        final SeekBar bar = (SeekBar) v.findViewById(R.id.seekBar_spindle_speed);
        final TextView title = (TextView) v.findViewById(R.id.spindle_speed_view);

        title.setText(String.format(getString(R.string.ctrl_dialog_spindle_speed_title), spindleSpeed));

        bar.setProgress(Utils.map(spindleSpeed, 255, 10));
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // handle values here
                int value = Utils.map(progress, 10, 255);
                spindleSpeed = value;
                title.setText(String.format(getString(R.string.ctrl_dialog_spindle_speed_title), spindleSpeed));
                sendMessageToSocket(String.format(getString(R.string.send_spindle_speed), value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setRedChannelSeekBar (View v) {
        final SeekBar bar = (SeekBar) v.findViewById(R.id.seekBar_light_red_channel);
        final TextView title = (TextView) v.findViewById(R.id.light_red_channel_view);

        title.setText(String.format(getString(R.string.ctrl_dialog_light_red_channel_title), redLightChannel));
        bar.setProgress(Utils.map(redLightChannel, 255, 10));
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // handle values here
                int value = Utils.map(progress, 10, 255);
                redLightChannel = value;
                title.setText(String.format(getString(R.string.ctrl_dialog_light_red_channel_title), redLightChannel));
                sendMessageToSocket(String.format(getString(R.string.send_red_channel), value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void setGreenChannelSeekBar (View v) {
        final SeekBar bar = (SeekBar) v.findViewById(R.id.seekBar_light_green_channel);
        final TextView title = (TextView) v.findViewById(R.id.light_green_channel_view);
        title.setText(String.format(getString(R.string.ctrl_dialog_light_green_channel_title), greenLightChannel));

        bar.setProgress(Utils.map(greenLightChannel, 255, 10));
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // handle values here
                int value = Utils.map(progress, 10, 255);
                greenLightChannel = value;
                title.setText(String.format(getString(R.string.ctrl_dialog_light_green_channel_title), greenLightChannel));
                sendMessageToSocket(String.format(getString(R.string.send_green_channel), value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void setBlueChannelSeekBar (View v) {
        final SeekBar bar = (SeekBar) v.findViewById(R.id.seekBar_light_blue_channel);
        final TextView title = (TextView) v.findViewById(R.id.light_blue_channel_view);
        title.setText(String.format(getString(R.string.ctrl_dialog_light_blue_channel_title), blueLightChannel));

        bar.setProgress(Utils.map(blueLightChannel, 255, 10));
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // handle values here
                int value = Utils.map(progress, 10, 255);
                blueLightChannel = value;
                title.setText(String.format(getString(R.string.ctrl_dialog_light_blue_channel_title), blueLightChannel));
                sendMessageToSocket(String.format(getString(R.string.send_blue_channel), value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }
}

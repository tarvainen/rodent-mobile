package rodent.rodentmobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class ManualControllerActivity extends AppCompatActivity {

    private WebSocketClient mWebSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_controller);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manual_controller, menu);
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

    private void connectWebSocket() {
        if (this.mWebSocketClient != null && this.mWebSocketClient.getConnection().isOpen()) {
            Log.d("joo", "closeee");
            this.mWebSocketClient.getConnection().closeConnection(0, "Close");
            return;
        }

        URI uri;
        try {
            uri = new URI("ws://192.168.1.8:5000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.d("WebSocket", "Connected");
            }

            @Override
            public void onMessage(String s) {
                Log.d("juuuu", s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.d("WebSocket", "Disconnected");
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

    public void buttonClick (View v) {
        switch (v.getId()) {
            case R.id.btn_connect:
                connectWebSocket();
                break;
            case R.id.btn_send_to_rodent:
                sendMessageToRodent();
            default:
                break;
        }
    }

    public void sendMessageToRodent () {
        TextView textView = (TextView) findViewById(R.id.txtArea_rodent_code);
        String msg = textView.getText().toString();
        if (this.mWebSocketClient != null && this.mWebSocketClient.getConnection().isOpen()) {
            this.mWebSocketClient.send(msg);
        }
    }

    @Override
    public void onStop () {
        super.onStop();
        if (this.mWebSocketClient != null) {
            this.mWebSocketClient.getConnection().closeConnection(0, "Closed");
        }
    }
}

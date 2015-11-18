package rodent.rodentmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import rodent.rodentmobile.R;

public class SplashActivity extends AppCompatActivity {

    private int SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run () {
                Intent intent = new Intent(SplashActivity.this, LibraryActivity.class);
                startActivity(intent);
            }
        }, SPLASH_DURATION);
    }

}

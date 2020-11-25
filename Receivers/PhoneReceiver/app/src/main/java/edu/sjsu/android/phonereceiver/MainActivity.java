package edu.sjsu.android.phonereceiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!checkPermission()) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    android.Manifest.permission.READ_PHONE_STATE,
                    android.Manifest.permission.READ_CALL_LOG
            }, 100);
        }
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_PHONE_STATE) ==
                        PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_CALL_LOG) ==
                        PackageManager.PERMISSION_GRANTED;
    }
}
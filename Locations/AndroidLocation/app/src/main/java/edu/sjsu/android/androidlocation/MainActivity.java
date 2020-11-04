package edu.sjsu.android.androidlocation;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnShowLocation;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        // Show Location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create class object
                gps = new GPSTracker(MainActivity.this);
                Location location = gps.getLocation();

                // Check if GPS enabled
                if (gps.canGetLocation()) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Toast.makeText(getApplicationContext(), "Your location is - \nLat: " +
                            latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
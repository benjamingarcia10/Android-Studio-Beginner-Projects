package edu.sjsu.android.exercise1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle myInput = this.getIntent().getExtras();

        TextView t = new TextView(this);
        t = (TextView) findViewById(R.id.textView2);
        t.setText(String.format("Hello %s!", myInput.getString("uname")));
    }
}
package edu.sjsu.android.mybrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        textView = (TextView) this.findViewById(R.id.textView);
        String url = getIntent().getDataString();
        textView.setText(url);
    }
}
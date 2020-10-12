package edu.sjsu.android.intenttester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityLoaderActivity extends AppCompatActivity {
    Button webButton;
    Button callButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webButton = (Button) this.findViewById(R.id.webButton);
        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.webButton) {
                    String webAddress = getResources().getString(R.string.web_address);
                    Uri webPage = Uri.parse(webAddress);
                    Intent myWebIntent = new Intent(Intent.ACTION_VIEW, webPage);
                    myWebIntent.putExtra("webAddress", webAddress);

                    String chooserText = getResources().getString(R.string.chooser_text, webAddress);
                    Intent chooser = Intent.createChooser(myWebIntent, chooserText);

                    if (myWebIntent.resolveActivity(getPackageManager()) != null)
                        startActivity(chooser);
                }
            }
        });

        callButton = (Button) this.findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.callButton) {
                    String phoneNumber = getResources().getString(R.string.phone_number);
                    Intent myCallIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
                    startActivity(myCallIntent);
                }
            }
        });
    }
}
package edu.sjsu.android.zoodirectory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class ZooInformationActivity extends MenuActivity implements View.OnClickListener {

    private Button buttonDial;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoo_information);

        buttonDial = (Button) this.findViewById(R.id.buttonDial);
        buttonDial.setOnClickListener(this);
    }

    public void onClick(View arg0) {
        Intent myIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getResources().getString(R.string.zoo_phone_number)));
        this.startActivity(myIntent);
    }

}

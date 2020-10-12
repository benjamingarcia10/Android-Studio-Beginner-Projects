package edu.sjsu.android.zoodirectory;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AnimalDetailActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_detail);
        Bundle myAnimalsInfo = this.getIntent().getExtras();

        ImageView animalImage = (ImageView) this.findViewById(R.id.animalImage);
        TextView animalName = (TextView) this.findViewById(R.id.animalName);
        TextView animalDescription = (TextView) this.findViewById(R.id.animalDescription);

        animalImage.setImageResource(myAnimalsInfo.getInt("animalImageResource"));
        animalName.setText(myAnimalsInfo.getString("animalName"));
        animalDescription.setText(myAnimalsInfo.getString("animalDescription"));
    }
}

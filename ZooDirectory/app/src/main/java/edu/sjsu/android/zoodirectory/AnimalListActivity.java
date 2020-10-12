package edu.sjsu.android.zoodirectory;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AnimalListActivity extends MenuActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Animal> animals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_list);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        this.addAnimals();

        // define an adapter
        mAdapter = new MyAdapter(this, animals);
        recyclerView.setAdapter(mAdapter);
    }

    private void addAnimals() {
        animals = new ArrayList<Animal>();
        animals.add(new Animal("Lion", getResources().getString(R.string.lion_description), R.drawable.lion));
        animals.add(new Animal("Giraffe", getResources().getString(R.string.giraffe_description), R.drawable.giraffe));
        animals.add(new Animal("Elephant", getResources().getString(R.string.elephant_description), R.drawable.elephant));
        animals.add(new Animal("Giant Panda", getResources().getString(R.string.giant_panda_description), R.drawable.giant_panda));
        animals.add(new Animal("Monkey", getResources().getString(R.string.monkey_description), R.drawable.monkey));
        animals.add(new Animal("Penguin", getResources().getString(R.string.penguin_description), R.drawable.penguin));
        animals.add(new Animal("Polar Bear", getResources().getString(R.string.polar_bear_description), R.drawable.polar_bear));
        animals.add(new Animal("Crocodile", getResources().getString(R.string.crocodile_description), R.drawable.crocodile));
    }
}
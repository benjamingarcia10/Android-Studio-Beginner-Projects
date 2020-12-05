package edu.sjsu.android.stocksearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.HashSet;
import java.util.Set;

public class StockDetailsActivity extends AppCompatActivity {
    private FavoritesAdapter adapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Set<String> favoriteTickers;
    private String ticker;
    private Button favoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(getString(R.string.favoritesPrefName), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        favoriteTickers = new HashSet<String>(sharedPreferences.getStringSet(getString(R.string.tickersPrefKey), new HashSet<String>()));

        setContentView(R.layout.stock_activity_layout);

        favoriteButton = findViewById(R.id.favButton);

        Bundle bundle = this.getIntent().getExtras();
        this.ticker = bundle.getString("Ticker");
        this.adapter = (FavoritesAdapter) bundle.get("Adapter");

        if (favoriteTickers.contains(this.ticker)) {
            favoriteButton.setText(getString(R.string.removeFromFavText));
        } else {
            favoriteButton.setText(getString(R.string.addToFavText));
        }

        RecyclerView recyclerViewCurrent = (RecyclerView) findViewById(R.id.recyclerViewCurrentInfo);
        RecyclerView recyclerViewHistorical = (RecyclerView)
                findViewById(R.id.recyclerViewHistoricalInfo);
        recyclerViewCurrent.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManagerCurrent = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManagerHistorical = new LinearLayoutManager(this);
        recyclerViewCurrent.setLayoutManager(layoutManagerCurrent);
        recyclerViewHistorical.setLayoutManager(layoutManagerHistorical);

        try {
            StockActivityAdapter stockAdapter = new StockActivityAdapter(this, bundle);
            HistoricalStockActivityAdapter historicalStockAdapter = new HistoricalStockActivityAdapter(this, bundle);
            recyclerViewCurrent.setAdapter(stockAdapter);
            recyclerViewHistorical.setAdapter(historicalStockAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onButtonFavorite(View view) {
        if (favoriteButton.getText().toString().equals(getString(R.string.addToFavText))) {
            favoriteTickers.add(ticker);
            editor.putStringSet(getString(R.string.tickersPrefKey), favoriteTickers);
            editor.apply();
            favoriteButton.setText(getString(R.string.removeFromFavText));
        } else if (favoriteButton.getText().toString().equals(getString(R.string.removeFromFavText))) {
            favoriteTickers.remove(ticker);
            editor.putStringSet(getString(R.string.tickersPrefKey), favoriteTickers);
            editor.apply();
            favoriteButton.setText(getString(R.string.addToFavText));
        }
    }
}

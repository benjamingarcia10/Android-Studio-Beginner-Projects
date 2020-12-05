package edu.sjsu.android.stocksearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private String TIINGO_API_KEY;
    private Context mContext;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Set<String> favoriteTickersSet;

    private ProgressBar circularProgressBar;
    private TextView fetchTextView;

    private AutoCompleteTextView stockSearchTextView;
    private ApplicationInfo ai;
    private Button clearButton;
    private Button getQuoteButton;
    private ImageButton refreshButton;

    private Switch autoRefreshSwitch;

    private ListView favoritesView;

    private SearchAdapter searchAdapter;
    private FavoritesAdapter favoritesAdapter;

    private boolean autoRefreshEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mContext = MainActivity.this;

        sharedPreferences = getSharedPreferences(getString(R.string.favoritesPrefName), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setContentView(R.layout.activity_main);

        try {
            ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            TIINGO_API_KEY = bundle.getString("edu.sjsu.android.stocksearch.TIINGO_API_KEY");
            Log.d("API_KEY", TIINGO_API_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR_TRACE", e.getLocalizedMessage());
            Log.e("API_ERROR", "API Key not found. Please configure API key in AndroidManifest.xml file.");
        }

        circularProgressBar = findViewById(R.id.circularProgressBar);
        fetchTextView = findViewById(R.id.fetchTextView);
        circularProgressBar.setVisibility(View.GONE);
        fetchTextView.setVisibility(View.GONE);

        stockSearchTextView = findViewById(R.id.autoCompleteTextView);
        clearButton = findViewById(R.id.clearButton);
        getQuoteButton = findViewById(R.id.getQuoteButton);
        refreshButton = findViewById(R.id.refreshButton);

        autoRefreshSwitch = findViewById(R.id.autoRefreshSwitch);
        autoRefreshEnabled = false;

        favoritesView = findViewById(R.id.favoritesView);
        favoritesAdapter = new FavoritesAdapter(this, new ArrayList<StockModel>());
        updateFavoritesList(favoritesAdapter);

        favoritesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StockModel stock = (StockModel) adapterView.getItemAtPosition(i);
                ValidationFetchTask fetchTask = new ValidationFetchTask(TIINGO_API_KEY,
                        stock.getTickerSymbol(), mContext, circularProgressBar, fetchTextView);
                fetchTask.execute();
            }
        });

        searchAdapter = new SearchAdapter(this, new ArrayList<StockModel>());
        stockSearchTextView.setAdapter(searchAdapter);
//        stockSearchTextView.requestFocus();

        stockSearchTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int textLength) {
                if (textLength >= 3) {
                    AutoCompleteFetchTask fetch = new AutoCompleteFetchTask(searchAdapter,
                            charSequence.toString(), TIINGO_API_KEY, circularProgressBar, fetchTextView);
                    fetch.execute();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stockSearchTextView.setText("");
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFavoritesList(favoritesAdapter);
            }
        });

        autoRefreshSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoRefreshEnabled = autoRefreshSwitch.isChecked();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFavoritesList(new FavoritesAdapter(this, new ArrayList<StockModel>()));
    }

    public void onQuoteButton(View view) {
        if (stockSearchTextView.getText().length() <= 0) {
            ErrorDialogFragment dialog = new ErrorDialogFragment("Error!",
                    "Please enter a Stock Name/Symbol", "OK");
            dialog.show(getSupportFragmentManager(), "EmptyEntryDialog");
        } else {
            ValidationFetchTask fetchTask = new ValidationFetchTask(TIINGO_API_KEY,
                    stockSearchTextView.getText().toString(), this,
                    circularProgressBar, fetchTextView);
            fetchTask.execute();
        }
    }

    private void updateFavoritesList(FavoritesAdapter adapter) {
        this.favoritesAdapter = adapter;
        favoritesView.setAdapter(adapter);
        favoriteTickersSet = new HashSet<String>(sharedPreferences.getStringSet(getString(R.string.tickersPrefKey), new HashSet<String>()));

        for (String fav : favoriteTickersSet) {
            FavoritesFetchTask favoritesFetchTask = new FavoritesFetchTask(adapter, fav, TIINGO_API_KEY, circularProgressBar, fetchTextView);
            favoritesFetchTask.execute();
        }

        adapter.notifyDataSetChanged();
    }
}
package edu.sjsu.android.stocksearch;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FavoritesFetchTask extends AsyncTask<Void, Void, Void> {
    private FavoritesAdapter adapter;
    private String ticker;
    private String tiingoApiKey;
    private String searchUrlString;
    private String fetchUrl;
    private String data;
    private ProgressBar circularProgressBar;
    private TextView fetchTextView;
    private StockModel stock;

    private JSONObject stockInfo;

    public FavoritesFetchTask(FavoritesAdapter adapter, String ticker, String tiingoApiKey, ProgressBar circularProgressBar, TextView fetchTextView) {
        this.adapter = adapter;
        this.ticker = ticker;
        this.tiingoApiKey = tiingoApiKey;
        this.circularProgressBar = circularProgressBar;
        this.fetchTextView = fetchTextView;
        this.searchUrlString = String.format("https://api.tiingo.com/tiingo/utilities/search?query=%s&exactTickerMatch=true&token=%s", this.ticker, this.tiingoApiKey);
        this.fetchUrl = String.format("https://api.tiingo.com/iex/?tickers=%s&token=%s", this.ticker, this.tiingoApiKey);
    }

    @Override
    protected void onPreExecute() {
        circularProgressBar.setVisibility(View.VISIBLE);
        fetchTextView.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            // Retrieve company name
            URL searchUrl = new URL(searchUrlString);
            HttpURLConnection searchUrlConnection = (HttpURLConnection) searchUrl.openConnection();
            InputStream searchUrlConnectionInputStream = searchUrlConnection.getInputStream();
            BufferedReader searchBr = new BufferedReader(new InputStreamReader(searchUrlConnectionInputStream));
            data = "";
            String searchLine;
            while ((searchLine = searchBr.readLine()) != null) {
                data = data + searchLine;
            }
            JSONArray searchJson = new JSONArray(data);
            String companyName = searchJson.getJSONObject(0).getString("name");

            // Get all other ticker info
            URL url = new URL(fetchUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            data = "";
            String line;
            while ((line = br.readLine()) != null) {
                data = data + line;
            }
            JSONArray json = new JSONArray(data);
            if (json.length() > 0) {
                stockInfo = json.getJSONObject(0);
                stock = new StockModel(stockInfo.getString("ticker"), companyName,
                        stockInfo.getDouble("open"),
                        stockInfo.getDouble("last"),
                        stockInfo.getDouble("volume"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FAVORITES_ERROR", e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        adapter.add(stock);
        circularProgressBar.setVisibility(View.GONE);
        fetchTextView.setVisibility(View.GONE);
    }
}

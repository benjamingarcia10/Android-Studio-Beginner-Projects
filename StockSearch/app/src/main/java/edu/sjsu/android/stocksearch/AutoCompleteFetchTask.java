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
import java.util.ArrayList;

public class AutoCompleteFetchTask extends AsyncTask<Void, Void, ArrayList<StockModel>> {
    private SearchAdapter adapter;
    private String ticker;
    private String tiingoApiKey;
    private String fetchUrl;
    private String data;
    private ProgressBar circularProgressBar;
    private TextView fetchTextView;

    public AutoCompleteFetchTask(SearchAdapter adapter, String ticker, String tiingoApiKey, ProgressBar circularProgressBar, TextView fetchTextView) {
        this.adapter = adapter;
        this.ticker = ticker;
        this.tiingoApiKey = tiingoApiKey;
        this.circularProgressBar = circularProgressBar;
        this.fetchTextView = fetchTextView;
        this.fetchUrl = String.format("https://api.tiingo.com/tiingo/utilities/search?query=%s&token=%s", this.ticker, this.tiingoApiKey);
    }

    @Override
    protected void onPreExecute() {
        circularProgressBar.setVisibility(View.VISIBLE);
        fetchTextView.setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<StockModel> doInBackground(Void... voids) {
        try {
            Log.d("TASK", "Updating adapter with tickers for: " + ticker);
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
            ArrayList<StockModel> stocks = new ArrayList<>();
            for (int i = 0; i < json.length(); i++) {
                JSONObject obj = json.getJSONObject(i);
                StockModel stock = new StockModel(obj.getString("ticker"), obj.getString("name"));
                stocks.add(stock);
            }
            return stocks;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AUTO_COMPLETE_ERROR", e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<StockModel> stocks) {
        for (StockModel stock: stocks) {
            adapter.add(stock);
        }
        circularProgressBar.setVisibility(View.GONE);
        fetchTextView.setVisibility(View.GONE);
        adapter.getFilter().filter(ticker, null);
    }
}

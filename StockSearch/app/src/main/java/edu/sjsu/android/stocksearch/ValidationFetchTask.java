package edu.sjsu.android.stocksearch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ValidationFetchTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private String ticker;
    private String text;
    private String tiingoApiKey;
    private String fetchUrl;
    private String historicalFetchUrl;
    private String data;
    private ProgressBar circularProgressBar;
    private TextView fetchTextView;

    private JSONObject stockInfo;
    private JSONArray historicalStockData;

    public ValidationFetchTask(String tiingoApiKey, String text, Context mContext,
                               ProgressBar circularProgressBar, TextView fetchTextView) {
        this.tiingoApiKey = tiingoApiKey;
        this.ticker = "";
        this.text = text;
        this.mContext = mContext;
        this.circularProgressBar = circularProgressBar;
        this.fetchTextView = fetchTextView;
        this.fetchUrl = String.format("https://api.tiingo.com/iex/?tickers=%s&token=%s", this.text, this.tiingoApiKey);
    }

    @Override
    protected void onPreExecute() {
        circularProgressBar.setVisibility(View.VISIBLE);
        fetchTextView.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
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
                ticker = stockInfo.getString("ticker").toUpperCase();
                if (ticker.equals(text.toUpperCase())) {
                    // Perform fetch request for Historical data
                    String date = "2020-11-01";
                    historicalFetchUrl = String.format("https://api.tiingo.com/tiingo/daily/%s/prices?startDate=%s&resampleFreq=daily&token=%s", ticker, date, tiingoApiKey);
                    URL urlHistorical = new URL(historicalFetchUrl);
                    HttpURLConnection connHistorical = (HttpURLConnection) urlHistorical.openConnection();
                    InputStream inputStreamHistorical = connHistorical.getInputStream();
                    BufferedReader brHistorical = new BufferedReader(new InputStreamReader(inputStreamHistorical));
                    String dataHistorical = "";
                    String lineHistorical;
                    while ((lineHistorical = brHistorical.readLine()) != null) {
                        dataHistorical = dataHistorical + lineHistorical;
                    }
                    historicalStockData = new JSONArray(dataHistorical);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("VALIDATION_ERROR", e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        circularProgressBar.setVisibility(View.GONE);
        fetchTextView.setVisibility(View.GONE);

        // Dialog for Invalid Ticker is in AutoComplete
        if (!ticker.equals(text.toUpperCase())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Invalid Stock Symbol!");
            builder.setMessage("Please Enter a Valid Stock Symbol");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else {
            Intent myIntent = new Intent(mContext, StockDetailsActivity.class);
            myIntent.putExtra("Ticker", ticker);
            myIntent.putExtra("JObject", stockInfo.toString());
            myIntent.putExtra("JObjectLength", stockInfo.length());
            myIntent.putExtra("JAHistorical", historicalStockData.toString());
            myIntent.putExtra("JAHistoricalLength", historicalStockData.length());
            mContext.startActivity(myIntent);
        }
    }
}

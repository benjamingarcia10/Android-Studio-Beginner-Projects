package edu.sjsu.android.stocksearch;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoricalStockActivityAdapter extends RecyclerView.Adapter<HistoricalStockActivityAdapter.ViewHolder> {
    private JSONArray JA;
    private Context mContext;
    private int listLength;

    public HistoricalStockActivityAdapter(Context mContext, Bundle bundle) throws JSONException {
        this.mContext = mContext;

        String stringJO = bundle.getString("JAHistorical");
        this.JA = new JSONArray(stringJO);

        this.listLength = bundle.getInt("JAHistoricalLength");

    }

    // Refers to the stock_activity_row_layout
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDate;
        public TextView textViewPrice;
        public TextView textViewChange;
        public View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.textViewDate = (TextView) view.findViewById(R.id.textViewDate);
            this.textViewPrice = (TextView) view.findViewById(R.id.textViewPrice);
            this.textViewChange = (TextView) view.findViewById(R.id.textViewPercent);
        }
    }

    @Override
    public HistoricalStockActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.historical_information_row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        HistoricalStockActivityAdapter.ViewHolder vh = new HistoricalStockActivityAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(HistoricalStockActivityAdapter.ViewHolder holder, int position) {
        try {
            JSONObject JO = (JSONObject) this.JA.get(position);
            double close_price = JO.getDouble("close");
            double open_price = JO.getDouble("open");
            double percentage = (close_price - open_price) / open_price * 100;
            percentage = Math.round(percentage * 100.0) / 100.0;

            String date = JO.get("date").toString();
            holder.textViewDate.setText(date.substring(0, 10));
            holder.textViewPrice.setText("$" + JO.getString("close"));
            if (percentage >= 0) {
                String stringPercentage = "+" + String.valueOf(percentage) + "%";
                holder.textViewChange.setText(stringPercentage);
            } else {
                String stringPercentage = "-" + String.valueOf(percentage) + "%";
                holder.textViewChange.setBackgroundColor(Color.RED);
                holder.textViewChange.setText(stringPercentage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.listLength;
    }

    // Get a List of the Parameters of JSON object wanted
    private ArrayList<String> getParameters() {
        ArrayList<String> newList = new ArrayList<>();
        newList.add("date");
        newList.add("timestamp");
        newList.add("last");
        return newList;
    }
}

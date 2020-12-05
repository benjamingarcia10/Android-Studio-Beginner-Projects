package edu.sjsu.android.stocksearch;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StockActivityAdapter extends RecyclerView.Adapter<StockActivityAdapter.ViewHolder> {
    private JSONObject JO;
    private ArrayList<String> listOfParameters;

    public StockActivityAdapter(Context mContext, Bundle bundle) throws JSONException {
        String stringJO = bundle.getString("JObject");
        this.JO = new JSONObject(stringJO);

        this.listOfParameters = this.getParameters();
    }

    // Refers to the stock_activity_row_layout
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewValueName;
        public TextView textViewValue;
        public View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.textViewValueName = (TextView) view.findViewById(R.id.textViewValueName);
            this.textViewValue = (TextView) view.findViewById(R.id.textViewValue);
        }
    }

    @Override
    public StockActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.stock_activity_row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(StockActivityAdapter.ViewHolder holder, int position) {
        String parameter = listOfParameters.get(position);

        holder.textViewValueName.setText(parameter);
        try {
            holder.textViewValue.setText(this.JO.get(parameter).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.listOfParameters.size();
    }

    // Get a List of the Parameters of JSON object wanted
    private ArrayList<String> getParameters() {
        ArrayList<String> newList = new ArrayList<>();
        newList.add("ticker");
        newList.add("timestamp");
        newList.add("last");
        newList.add("prevClose");
        newList.add("open");
        newList.add("high");
        newList.add("low");
        newList.add("mid");
        newList.add("volume");
        newList.add("bidSize");
        newList.add("bidPrice");
        newList.add("askSize");
        newList.add("askPrice");

        return newList;
    }
}

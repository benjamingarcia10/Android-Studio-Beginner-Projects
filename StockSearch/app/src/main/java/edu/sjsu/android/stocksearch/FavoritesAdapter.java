package edu.sjsu.android.stocksearch;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FavoritesAdapter extends ArrayAdapter<StockModel> {
    private ArrayList<StockModel> stocks;
    private Context mContext;

    public FavoritesAdapter(Context mContext, ArrayList<StockModel> stocks) {
        super(mContext, R.layout.favorites_row_layout, stocks);
        this.mContext = mContext;
        this.stocks = stocks;
    }

    private static class ViewHolder {
        TextView favTickerSymbol;
        TextView favCompanyNameTextView;
        TextView favPriceTextView;
        TextView favPercentTextView;
        TextView favMarCapTextView;
    }

    @Override
    public void add(StockModel stock) {
        if (!stocks.contains(stock)) {
            stocks.add(stock);
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StockModel stock = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.favorites_row_layout, parent, false);

            viewHolder.favTickerSymbol = convertView.findViewById(R.id.favTickerSymbol);
            viewHolder.favCompanyNameTextView = convertView.findViewById(R.id.favCompanyNameTextView);
            viewHolder.favPriceTextView = convertView.findViewById(R.id.favPriceTextView);
            viewHolder.favPercentTextView = convertView.findViewById(R.id.favPercentTextView);
            viewHolder.favMarCapTextView = convertView.findViewById(R.id.favMarCapTextView);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.favTickerSymbol.setText(stock.getTickerSymbol());
        viewHolder.favCompanyNameTextView.setText(stock.getCompanyName());
        viewHolder.favPriceTextView.setText(Double.toString(stock.getCurrentPrice()));

//        String percent = "";
        Double percent = ((stock.getCurrentPrice() - stock.getPreviousPrice()) / stock.getPreviousPrice()) * 100;
        percent = Math.round(percent * 100.0) / 100.0;

        if (percent < 0) {
            viewHolder.favPercentTextView.setText("-" + percent.toString() + "%");
            viewHolder.favPercentTextView.setBackgroundColor(Color.RED);
        } else if (percent > 0) {
            viewHolder.favPercentTextView.setText("+" + percent.toString() + "%");
            viewHolder.favPercentTextView.setBackgroundColor(Color.GREEN);
        } else {
            viewHolder.favPercentTextView.setText(percent.toString() + "%");
            viewHolder.favPercentTextView.setBackgroundColor(Color.WHITE);
        }

        viewHolder.favMarCapTextView.setText(String.format("$%.2f", stock.getMarketCap()));

        return result;
    }

    @Override
    public int getCount() {
        return stocks.size();
    }

    @Override
    public StockModel getItem(int position) {
        return stocks.get(position);
    }
}

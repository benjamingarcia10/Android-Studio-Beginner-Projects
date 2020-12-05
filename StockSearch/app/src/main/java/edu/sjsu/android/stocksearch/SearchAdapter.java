package edu.sjsu.android.stocksearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchAdapter extends ArrayAdapter<StockModel> {
    private ArrayList<StockModel> stocks, filteredStocks;
    private Context mContext;

    public SearchAdapter(Context mContext, ArrayList<StockModel> stocks) {
        super(mContext, R.layout.search_row_layout, stocks);
        this.mContext = mContext;
        this.stocks = stocks;
        filteredStocks = new ArrayList<>();
    }

    private static class ViewHolder {
        TextView tickerTextView;
        TextView companyNameTextView;
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
            convertView = inflater.inflate(R.layout.search_row_layout, parent, false);
            viewHolder.tickerTextView = convertView.findViewById(R.id.tickerTextView);
            viewHolder.companyNameTextView = convertView.findViewById(R.id.companyNameTextView);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.tickerTextView.setText(stock.getTickerSymbol());
        viewHolder.companyNameTextView.setText(stock.getCompanyName());

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

    @Override
    public Filter getFilter() {
        return stockFilter;
    }

    Filter stockFilter = new Filter() {
        // Displayed when clicked on in dropdown for auto complete
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            StockModel stock = (StockModel) resultValue;
            return stock.getTickerSymbol();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                filteredStocks.clear();
                for (StockModel stock: stocks) {
                    if (stock.getCompanyName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredStocks.add(stock);
                    } else if (stock.getTickerSymbol().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredStocks.add(stock);
                    }
                }
                filterResults.values = filteredStocks;
                filterResults.count = filteredStocks.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<StockModel> filteredList = (List<StockModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                Collections.sort(filteredList);
                for (StockModel stock : filteredList) {
                    add(stock);
                }
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };
}

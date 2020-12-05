package edu.sjsu.android.stocksearch;

import java.util.Objects;

public class StockModel implements Comparable {
    private String tickerSymbol;
    private String companyName;

    private Double previousPrice;
    private Double currentPrice;
    private Double volume;

    public StockModel(String tickerSymbol, String companyName) {
        this.tickerSymbol = tickerSymbol;
        this.companyName = companyName;
    }

    public StockModel(String tickerSymbol, String companyName, Double previousPrice, Double currentPrice, Double volume) {
        this.tickerSymbol = tickerSymbol;
        this.companyName = companyName;
        this.previousPrice = previousPrice;
        this.currentPrice = currentPrice;
        this.volume = volume;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Double getPreviousPrice() {
        return previousPrice;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public Double getVolume() {
        return volume;
    }

    public Double getMarketCap() {
        return volume * currentPrice;
    }

    @Override
    public String toString() {
        return tickerSymbol + " " + companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockModel that = (StockModel) o;
        return Objects.equals(tickerSymbol, that.tickerSymbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tickerSymbol);
    }

    @Override
    public int compareTo(Object o) {
        return this.tickerSymbol.compareTo(o.toString());
    }
}

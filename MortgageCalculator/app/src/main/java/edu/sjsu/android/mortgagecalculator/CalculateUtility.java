package edu.sjsu.android.mortgagecalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculateUtility {

    public static double calculateMonthly(Double principal, Double annualInterestRate, int loanLengthInYears, boolean includeTaxesAndInsurance) {
        double taxes = 0;
        double monthlyInterestDecimal = annualInterestRate / 1200;
        int loanLengthInMonths = loanLengthInYears * 12;

        if (includeTaxesAndInsurance) {
            taxes = principal * 0.001;
        }

        if (annualInterestRate == 0) {
            return round((principal / loanLengthInMonths) + taxes, 2);
        } else {
            Double numerator = monthlyInterestDecimal;
            Double denominator = 1 - Math.pow(1 + monthlyInterestDecimal, -loanLengthInMonths);
            return round((principal * (numerator / denominator)) + taxes, 2);
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}

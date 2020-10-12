package edu.sjsu.android.mortgagecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    EditText amountBorrowedText;
    SeekBar interestRateSeekBar;
    TextView interestRateText;
    RadioGroup loanTermGroupButtons;
    RadioButton loan15;
    RadioButton loan20;
    RadioButton loan30;
    CheckBox includeTaxesCheckBox;
    Button calculateButton;
    TextView monthlyPaymentText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amountBorrowedText = (EditText) this.findViewById(R.id.inputAmount);
        interestRateSeekBar = (SeekBar) this.findViewById(R.id.interestRateSeekBar);
        interestRateText = (TextView) this.findViewById(R.id.interestRateText);
        loanTermGroupButtons = (RadioGroup) this.findViewById(R.id.loanTermGroup);
        loan15 = (RadioButton) this.findViewById(R.id.loan15Button);
        loan20 = (RadioButton) this.findViewById(R.id.loan20Button);
        loan30 = (RadioButton) this.findViewById(R.id.loan30Button);
        includeTaxesCheckBox = (CheckBox) this.findViewById(R.id.taxesCheckBox);
        calculateButton = (Button) this.findViewById(R.id.calculateButton);
        monthlyPaymentText = (TextView) this.findViewById(R.id.monthlyPaymentText);

        interestRateSeekBar.setOnSeekBarChangeListener(this);
        calculateButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Double principal = 0.0;

        try {
            principal = Double.parseDouble(amountBorrowedText.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(context, R.string.amountInputError, duration).show();
            return;
        }

        int selectedButtonId = loanTermGroupButtons.getCheckedRadioButtonId();
        int loanTerm = 0;
        if (selectedButtonId == loan15.getId()) {
            loanTerm = 15;
        } else if (selectedButtonId == loan20.getId()) {
            loanTerm = 20;
        } else if (selectedButtonId == loan30.getId()) {
            loanTerm = 30;
        }

        Double annualInterestRate = formatSeekBar(interestRateSeekBar.getProgress());

        Double calculatedMonthly = CalculateUtility.calculateMonthly(principal, annualInterestRate, loanTerm, includeTaxesCheckBox.isChecked());
        monthlyPaymentText.setText("$" + calculatedMonthly);
    }

    private Double formatSeekBar(int i) {
        return i / 10.0;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        Double calculatedProgress = formatSeekBar(i);
        interestRateText.setText(calculatedProgress + "%");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

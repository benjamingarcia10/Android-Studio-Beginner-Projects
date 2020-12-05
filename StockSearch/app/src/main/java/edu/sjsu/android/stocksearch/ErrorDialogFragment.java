package edu.sjsu.android.stocksearch;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

public class ErrorDialogFragment extends androidx.fragment.app.DialogFragment {
    private String title;
    private String message;
    private String buttonText;

    public ErrorDialogFragment(String title, String message, String buttonText) {
        this.title = title;
        this.message = message;
        this.buttonText = buttonText;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }
}

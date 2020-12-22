package com.knowhouse.mobilestoreapplication.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.knowhouse.mobilestoreapplication.Interfaces.DialogResponseInterface;
import com.knowhouse.mobilestoreapplication.R;

public class PromptUserFragment extends DialogFragment{

    private DialogResponseInterface responseInterface;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //use the builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.okayButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialogResponseInterface myDialog = (DialogResponseInterface) dialog;
                        myDialog.DialogResponse(which);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //user cancelled the dialog
                        DialogResponseInterface myDialog = (DialogResponseInterface) dialog;
                        myDialog.DialogResponse(which);
                    }
                });

        //create the alert Dialog
        return builder.create();
    }
}
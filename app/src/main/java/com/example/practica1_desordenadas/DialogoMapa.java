package com.example.practica1_desordenadas;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogoMapa extends DialogFragment {
    AlertDialog.Builder builder;

    @Override
public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    super.onCreateDialog(savedInstanceState);
    builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(getString(R.string.enhorabuenaDestino));
    builder.setMessage(getString(R.string.enhorabuenaDestino));

    builder.setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    });



    AlertDialog dialog=builder.create();
    dialog.setCanceledOnTouchOutside(false);
    return dialog;
}
}

package com.example.practica1_desordenadas;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogoFinNivel extends DialogFragment {
    //Dialogo fin de nivel
    AlertDialog.Builder builder;
    public interface ListenerdelDialogoFinNivel {
        void alpulsarMenuNiveles();
        void alpulsarSiguienteNivel();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.finPartida));
        builder.setMessage(getString(R.string.eligeOpcion));
        DialogoFinNivel.ListenerdelDialogoFinNivel miListener = (DialogoFinNivel.ListenerdelDialogoFinNivel) getActivity();

        builder.setPositiveButton(getString(R.string.siguienteNivel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListener.alpulsarSiguienteNivel();
            }
        });

        builder.setNegativeButton(getString(R.string.menuNiveles), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListener.alpulsarMenuNiveles();
            }
        });


        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


}